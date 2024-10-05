package com.talestonini.db

import cats.effect.{IO, SyncIO}
import com.talestonini.db.model._
import com.talestonini.utils._
import java.time.ZonedDateTime
import munit.CatsEffectSuite

class CloudFirestoreSuite extends CatsEffectSuite {

  var testToken: Option[String] = None
  def getTestToken()            = testToken.getOrElse(throw new Exception("test token is invalid"))

  test("get an auth token") {
    CloudFirestore.getAuthToken() flatMap { token =>
      testToken = Some(token)
      IO(assertEquals(token.substring(0, 5), "eyJhb"))
    }
  }

  val postsPath        = "projects/ttdotcom/databases/(default)/documents/posts"
  val unitTestPostPath = "projects/ttdotcom/databases/(default)/documents/posts/unitTestPost"

  test("get a post") {
    // let's get the post about Scala Decorators
    CloudFirestore.getDocuments[Post](getTestToken(), postsPath) flatMap { posts =>
      val filteredPosts = posts.filter(p => p.fields.title.get == "Unit Test Post")
      IO(assertEquals(filteredPosts.size, 1))
      val decoratorsInScalaPost = filteredPosts(0)
      IO(assertEquals(decoratorsInScalaPost.name, unitTestPostPath))
    }
  }

  val unitTestCommentsPath = s"$unitTestPostPath/comments"
  val niceCommentPath      = s"$unitTestCommentsPath/${randomAlphaNumericString(20)}"

  test("create a comment") {
    val heather     = User(Some("Heather Miller"), Some("heather.miller@cs.cmu.edu"), Some("111"))
    val text        = "Great post!\n\nI really enjoyed reading it.\n\nThanks!"
    val niceComment = Comment(Some(heather), Some(ZonedDateTime.now()), Some(text))

    CloudFirestore.upsertDocument[Comment](getTestToken(), niceCommentPath, niceComment) flatMap { comment =>
      IO(assertEquals(comment.fields.author, niceComment.author))
      IO(assertEquals(comment.fields.date.toString(), niceComment.date.toString().substring(0, 23)))
      IO(assertEquals(comment.fields.text, niceComment.text))
    }
  }

  test("get a comment") {
    CloudFirestore.getDocuments[Comment](getTestToken(), unitTestCommentsPath) flatMap { comments =>
      val filteredComments = comments.filter(c => c.fields.author.get.name.get == "Heather Miller")
      IO(assertEquals(filteredComments.size, 1))
      val theNiceComment = filteredComments(0)
      IO(assertEquals(theNiceComment.name, niceCommentPath))
    }
  }

  test("delete a comment") {
    CloudFirestore.deleteDocument[Comment](getTestToken(), niceCommentPath) flatMap { response =>
      IO(assertEquals(response, None))
    }
  }

}
