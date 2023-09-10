package com.talestonini

import cats.effect.unsafe.implicits.global
import com.raquo.laminar.api.*
import com.raquo.laminar.api.L.{*, given}
import com.raquo.waypoint.*
import com.talestonini.components.{Logo, Menu, Footer, Spinner}
import com.talestonini.db.CloudFirestore
import com.talestonini.db.model.*
import com.talestonini.pages.*
import com.talestonini.pages.sourcegen.*
import com.talestonini.pages.sourcegen.posts.*
import com.talestonini.utils.{prismHighlightAll, sendGtagEvent}
import org.scalajs.dom
import scala.concurrent.Promise
import scala.scalajs.concurrent.JSExecutionContext.queue
import scala.util.{Failure, Success}
import upickle.default.*

@main
def TalesToniniDotCom(): Unit = {
  // bootstraps Laminar by installing a Laminar Element in an existing DOM element
  renderOnDomContentLoaded(
    org.scalajs.dom.document.getElementById("app-container"),
    App()
  )
}

object App {

  // --- UI ------------------------------------------------------------------------------------------------------------

  def apply(): Element = {
    retrievePostsDataFromDb()

    div(
      div(
        className := "w3-content w3-row w3-hide-small",
        div(
          className := "w3-padding-16",
          Logo(),
          Menu()
        ),
        hr()
      ),
      div(
        className := "w3-content w3-row w3-hide-large w3-hide-medium",
        div(
          className := "w3-padding-8",
          Logo(),
          Menu(isMobile = true)
        ),
        hr()
      ),
      div(
        className := "w3-content",
        div(
          className := "content w3-padding-16",
          Spinner(),
          child <-- router.currentPageSignal.map(render)
        ),
        hr()
      ),
      div(
        className := "footer w3-container w3-padding-16 w3-center w3-hide-small",
        Footer()
      ),
      div(
        className := "footer w3-container w3-padding-16 w3-center w3-hide-large w3-hide-medium",
        Footer()
      )
    )
  }

  // --- public --------------------------------------------------------------------------------------------------------

  sealed trait Page derives ReadWriter
  case object HomePage                 extends Page
  case object PostsPage                extends Page
  case object TagsPage                 extends Page
  case object AboutPage                extends Page
  case object DbLayerRefactorPage      extends Page
  case object ScalaDecoratorsPage      extends Page
  case object DockerVimPage            extends Page
  case object MorseCodeChallengePage   extends Page
  case object UrbanForestChallengePage extends Page
  case object FunProgCapstonePage      extends Page

  def navigateTo(page: Page): Binder[HtmlElement] = Binder { el =>
    val isLinkElement = el.ref.isInstanceOf[dom.html.Anchor]
    if (isLinkElement) {
      el.amend(href(router.absoluteUrlForPage(page)))
    }

    // If element is a link and user is holding a modifier while clicking:
    //  - Do nothing, browser will open the URL in new tab / window / etc. depending on the modifier key
    // Otherwise:
    //  - Perform regular pushState transition
    (onClick
      .filter(ev => !(isLinkElement && (ev.ctrlKey || ev.metaKey || ev.shiftKey || ev.altKey)))
      .preventDefault
      --> (_ => router.pushState(page))).bind(el)
  }

  def navigateByPostResource(resource: String) =
    navigateTo(postsMap(resource).page)

  // --- private -------------------------------------------------------------------------------------------------------

  // TODO: lazily load page's contents (it avoids downloading all images at startup)
  private case class PageEntry(path: String, element: Element)
  private val pagesMap: Map[Page, PageEntry] = Map(
    HomePage                 -> PageEntry("", DbLayerRefactor()),
    PostsPage                -> PageEntry("posts", Posts()),
    TagsPage                 -> PageEntry("tags", Tags()),
    AboutPage                -> PageEntry("about", About()),
    DbLayerRefactorPage      -> PageEntry("dbLayerRefactor", DbLayerRefactor()),
    ScalaDecoratorsPage      -> PageEntry("scalaDecorators", ScalaDecorators()),
    DockerVimPage            -> PageEntry("dockerVim", DockerVim()),
    MorseCodeChallengePage   -> PageEntry("morseCodeChallenge", MorseCodeChallenge()),
    UrbanForestChallengePage -> PageEntry("urbanForestChallenge", UrbanForestChallenge()),
    FunProgCapstonePage      -> PageEntry("funProgCapstone", FunProgCapstone())
  )

  private def buildRoute(page: Page) =
    if (page == HomePage)
      Route.static(HomePage, root / endOfSegments, basePath = Route.fragmentBasePath)
    else
      Route.static(page, root / pagesMap.get(page).get.path / endOfSegments, basePath = Route.fragmentBasePath)

  private val router = new Router[Page](
    routes = pagesMap.keySet.map(pe => buildRoute(pe)).toList,
    getPageTitle = _.toString,                 // mock page title (displayed in the browser tab next to favicon)
    serializePage = page => write(page),       // serialize page data for storage in History API log
    deserializePage = pageStr => read(pageStr) // deserialize the above
  )(
    popStateEvents = L.windowEvents(_.onPopState), // this is how Waypoint avoids an explicit dependency on Laminar
    owner = L.unsafeWindowOwner                    // this router will live as long as the window
  )

  private def render(page: Page): Element = {
    val pageEntry = pagesMap.get(page).get
    sendGtagEvent("page_view", pageEntry.path)
    prismHighlightAll() // in lieu of '<body onhashchange=...' because Waypoint does not trigger the hashchange event
    pageEntry.element
  }

  // maps post resource names to corresponding page and promise
  // (the post promise, which is fulfilled when posts data is retrieved from the database)
  private case class PostEntry(page: Page, promise: Promise[Doc[Post]])
  private val postsMap: Map[String, PostEntry] = Map(
    "dbLayerRefactor"      -> PostEntry(DbLayerRefactorPage, DbLayerRefactor.postDocPromise),
    "scalaDecorators"      -> PostEntry(ScalaDecoratorsPage, ScalaDecorators.postDocPromise),
    "dockerVim"            -> PostEntry(DockerVimPage, DockerVim.postDocPromise),
    "morseCodeChallenge"   -> PostEntry(MorseCodeChallengePage, MorseCodeChallenge.postDocPromise),
    "urbanForestChallenge" -> PostEntry(UrbanForestChallengePage, UrbanForestChallenge.postDocPromise),
    "funProgCapstone"      -> PostEntry(FunProgCapstonePage, FunProgCapstone.postDocPromise)
  )

  private def retrievePostsDataFromDb(): Unit = {
    val retrievingPosts = "retrievingPosts"
    Spinner.start(retrievingPosts)
    CloudFirestore
      .getPosts()
      .unsafeToFuture()
      .onComplete({
        case s: Success[Docs[Post]] =>
          for (
            postDoc <- s.get
            if postDoc.fields.enabled.getOrElse(true)
          ) {
            val resource = postDoc.fields.resource.get

            // to build the posts page, with the list of posts
            if (postsMap.keySet.contains(resource))
              Posts.posts.update(data => data :+ postDoc)

            // to build each post page
            postsMap
              .get(resource)
              .getOrElse(
                throw new Exception(s"missing entry in postsMap for $resource")
              )
              .promise success postDoc // fulfills the post promise
          }
          Spinner.stop(retrievingPosts)
        case f: Failure[Docs[Post]] =>
          println(s"failed getting posts: ${f.exception.getMessage()}")
          Spinner.stop(retrievingPosts)
      })(queue)
  }

}
