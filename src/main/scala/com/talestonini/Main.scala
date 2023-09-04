package com.talestonini

import cats.effect.unsafe.implicits.global
import com.raquo.laminar.api.L.{*, given}
import com.talestonini.components.{Logo, Menu, Footer}
import com.talestonini.db.CloudFirestore
import com.talestonini.db.model.*
import com.talestonini.pages.*
import com.talestonini.pages.sourcegen.posts.*
import com.talestonini.utils.*
import com.talestonini.utils.javascript.*
import scala.concurrent.Promise
import scala.scalajs.concurrent.JSExecutionContext.queue
import scala.util.{Failure, Success}

@main
def TalesToniniDotCom(): Unit =
  // bootstraps Laminar by installing a Laminar Element in an existing DOM element
  renderOnDomContentLoaded(
    org.scalajs.dom.document.getElementById("app"),
    Main()
  )

object Main {

  // --- state ---------------------------------------------------------------------------------------------------------

  // whether the app is loading some content or not;
  // this is the state that backs displaying a loading animation of some kind on the UI
  val isLoading: Var[Boolean] = Var(false)

  private val postDocMap: Map[String, Promise[Doc[Post]]] = Map(
    "dbLayerRefactor"      -> DbLayerRefactor.postDocPromise,
    "dockerVim"            -> DockerVim.postDocPromise,
    "funProgCapstone"      -> FunProgCapstone.postDocPromise,
    "morseCodeChallenge"   -> MorseCodeChallenge.postDocPromise,
    "scalaDecorators"      -> ScalaDecorators.postDocPromise,
    "urbanForestChallenge" -> UrbanForestChallenge.postDocPromise
  )

  private val pageMap: Map[String, Element] = Map(
    "" -> DbLayerRefactor(),
    // "about" -> About(),
    // "posts" -> Posts(),
    "tags" -> Tags(),
    // posts
    "funProgCapstone"      -> FunProgCapstone(),
    "dbLayerRefactor"      -> DbLayerRefactor(),
    "dockerVim"            -> DockerVim(),
    "morseCodeChallenge"   -> MorseCodeChallenge(),
    "scalaDecorators"      -> ScalaDecorators(),
    "urbanForestChallenge" -> UrbanForestChallenge()
  )

  // --- UI ------------------------------------------------------------------------------------------------------------

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
          div(
            className := "w3-center",
            styleAttr <-- isLoading.signal.map(b => s"display: ${jsDisplay(b)}"),
            p(i(className := "w3-xxxlarge fa fa-spinner w3-spin"))
          ),
          DbLayerRefactor()
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

  // -------------------------------------------------------------------------------------------------------------------

  // retrieve posts from db at application start
  private val retrievingPosts = "retrievingPosts"
  displayLoading(isLoading, retrievingPosts)
  CloudFirestore
    .getPosts()
    .unsafeToFuture()
    .onComplete({
      case s: Success[Docs[Post]] =>
        for (postDoc <- s.get if postDoc.fields.enabled.getOrElse(true)) {
          val resource = postDoc.fields.resource.get

          // to build the posts page, with the list of posts
          // if (pageMap.keySet.contains(resource))
          //   Posts.bPostLinks.value += Posts.BPostLink(
          //     title = Var(p.fields.title.get),
          //     firstPublishDate = Var(datetime2Str(p.fields.firstPublishDate)),
          //     publishDate = Var(datetime2Str(p.fields.publishDate)),
          //     resource = Var(resource)
          //   )

          // to build each post page
          postDocMap
            .get(resource)
            .getOrElse(
              throw new Exception(s"missing entry in postDocMap for $resource")
            ) success postDoc // fulfills the post promise
        }
        hideLoading(isLoading, retrievingPosts)
      case f: Failure[Docs[Post]] =>
        println(s"failed getting posts: ${f.exception.getMessage()}")
        hideLoading(isLoading, retrievingPosts)
    })(queue)

}
