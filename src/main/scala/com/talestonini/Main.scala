package com.talestonini

import cats.effect.unsafe.implicits.global
import com.raquo.laminar.api.L.{*, given}
import com.talestonini.components.{Logo, Menu, Footer, Spinner}
import com.talestonini.db.CloudFirestore
import com.talestonini.db.model.*
import com.talestonini.pages.*
import com.talestonini.pages.sourcegen.posts.*
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

  private val postDocMap: Map[String, Promise[Doc[Post]]] = Map(
    "dbLayerRefactor"      -> DbLayerRefactorPage.postDocPromise,
    "dockerVim"            -> DockerVimPage.postDocPromise,
    "funProgCapstone"      -> FunProgCapstonePage.postDocPromise,
    "morseCodeChallenge"   -> MorseCodeChallengePage.postDocPromise,
    "scalaDecorators"      -> ScalaDecoratorsPage.postDocPromise,
    "urbanForestChallenge" -> UrbanForestChallengePage.postDocPromise
  )

  private val pageMap: Map[String, Element] = Map(
    "" -> DbLayerRefactorPage(),
    // "about" -> AboutPage(),
    // "posts" -> PostsPage(),
    "tags" -> TagsPage(),
    // posts
    "funProgCapstone"      -> FunProgCapstonePage(),
    "dbLayerRefactor"      -> DbLayerRefactorPage(),
    "dockerVim"            -> DockerVimPage(),
    "morseCodeChallenge"   -> MorseCodeChallengePage(),
    "scalaDecorators"      -> ScalaDecoratorsPage(),
    "urbanForestChallenge" -> UrbanForestChallengePage()
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
          Spinner(),
          // DbLayerRefactorPage()
          PostsPage()
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
  Spinner.start(retrievingPosts)
  CloudFirestore
    .getPosts()
    .unsafeToFuture()
    .onComplete({
      case s: Success[Docs[Post]] =>
        for (postDoc <- s.get if postDoc.fields.enabled.getOrElse(true)) {
          val resource = postDoc.fields.resource.get

          // to build the posts page, with the list of posts
          if (pageMap.keySet.contains(resource))
            PostsPage.posts.update(data => data :+ postDoc)

          // to build each post page
          postDocMap
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
