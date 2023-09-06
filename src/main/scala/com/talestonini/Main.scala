package com.talestonini

import cats.effect.unsafe.implicits.global
import com.raquo.laminar.api.L.{*, given}
import com.talestonini.components.Spinner
import com.talestonini.db.CloudFirestore
import com.talestonini.db.model.*
import com.talestonini.pages.*
import com.talestonini.pages.sourcegen.posts.*
import scala.concurrent.Promise
import scala.scalajs.concurrent.JSExecutionContext.queue
import scala.util.{Failure, Success}

@main
def TalesToniniDotCom(): Unit = {
  Main.retrievePostsDbData()

  // bootstraps Laminar by installing a Laminar Element in an existing DOM element
  renderOnDomContentLoaded(
    org.scalajs.dom.document.getElementById("app-container"),
    App()
  )
}

object Main {

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

  def retrievePostsDbData(): Unit = {
    val retrievingPosts = "retrievingPosts"
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
              Posts.posts.update(data => data :+ postDoc)

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

}
