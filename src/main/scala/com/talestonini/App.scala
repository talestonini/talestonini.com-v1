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

val pageMap: Map[String, Promise[Doc[Post]]] = Map(
  "dbLayerRefactor"      -> DbLayerRefactor.postDocPromise,
  "scalaDecorators"      -> ScalaDecorators.postDocPromise,
  "dockerVim"            -> DockerVim.postDocPromise,
  "morseCodeChallenge"   -> MorseCodeChallenge.postDocPromise,
  "urbanForestChallenge" -> UrbanForestChallenge.postDocPromise,
  "funProgCapstone"      -> FunProgCapstone.postDocPromise
)

val homeRoute                 = Route.static(HomePage, root / endOfSegments)
val postsRoute                = Route.static(PostsPage, root / "posts" / endOfSegments)
val tagsRoute                 = Route.static(TagsPage, root / "tags" / endOfSegments)
val aboutRoute                = Route.static(AboutPage, root / "about" / endOfSegments)
val dbLayerRefactorRoute      = Route.static(DbLayerRefactorPage, root / "dbLayerRefactor" / endOfSegments)
val scalaDecoratorsRoute      = Route.static(ScalaDecoratorsPage, root / "scalaDecorators" / endOfSegments)
val dockerVimRoute            = Route.static(DockerVimPage, root / "dockerVim" / endOfSegments)
val morseCodeChallengeRoute   = Route.static(MorseCodeChallengePage, root / "morseCodeChallenge" / endOfSegments)
val urbanForestChallengeRoute = Route.static(UrbanForestChallengePage, root / "urbanForestChallenge" / endOfSegments)
val funProgCapstoneRoute      = Route.static(FunProgCapstonePage, root / "funProgCapstone" / endOfSegments)

val router = new Router[Page](
  routes = List(
    homeRoute,
    postsRoute,
    tagsRoute,
    aboutRoute,
    dbLayerRefactorRoute,
    scalaDecoratorsRoute,
    dockerVimRoute,
    morseCodeChallengeRoute,
    urbanForestChallengeRoute,
    funProgCapstoneRoute
  ),
  getPageTitle = _.toString,                 // mock page title (displayed in the browser tab next to favicon)
  serializePage = page => write(page),       // serialize page data for storage in History API log
  deserializePage = pageStr => read(pageStr) // deserialize the above
)(
  popStateEvents = L.windowEvents(_.onPopState), // this is how Waypoint avoids an explicit dependency on Laminar
  owner = L.unsafeWindowOwner                    // this router will live as long as the window
)

// lazy page elements
lazy val posts                = Posts()
lazy val tags                 = Tags()
lazy val about                = About()
lazy val dbLayerRefactor      = DbLayerRefactor()
lazy val scalaDecorators      = ScalaDecorators()
lazy val dockerVim            = DockerVim()
lazy val morseCodeChallenge   = MorseCodeChallenge()
lazy val urbanForestChallenge = UrbanForestChallenge()
lazy val funProgCapstone      = FunProgCapstone()

def routeTo(page: Page): Element = {
  page match {
    case HomePage                 => dbLayerRefactor
    case PostsPage                => posts
    case TagsPage                 => tags
    case AboutPage                => about
    case DbLayerRefactorPage      => dbLayerRefactor
    case ScalaDecoratorsPage      => scalaDecorators
    case DockerVimPage            => dockerVim
    case MorseCodeChallengePage   => morseCodeChallenge
    case UrbanForestChallengePage => urbanForestChallenge
    case FunProgCapstonePage      => funProgCapstone
  }
}

object App {

  def apply(): Element =
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
          child <-- router.currentPageSignal.map(routeTo)
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

  def navigateToPostByResource(resource: String) =
    navigateTo(resource match {
      case "dbLayerRefactor"      => DbLayerRefactorPage
      case "scalaDecorators"      => ScalaDecoratorsPage
      case "dockerVim"            => DockerVimPage
      case "morseCodeChallenge"   => MorseCodeChallengePage
      case "urbanForestChallenge" => UrbanForestChallengePage
      case "funProgCapstone"      => FunProgCapstonePage
    })

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
          if (pageMap.keySet.contains(resource))
            Posts.posts.update(data => data :+ postDoc)

          // to build each post page
          pageMap
            .get(resource)
            .getOrElse(
              throw new Exception(s"missing entry in postDocMap for $resource")
            ) success postDoc // fulfills the post promise
        }
        Spinner.stop(retrievingPosts)
      case f: Failure[Docs[Post]] =>
        println(s"failed getting posts: ${f.exception.getMessage()}")
        Spinner.stop(retrievingPosts)
    })(queue)

}
