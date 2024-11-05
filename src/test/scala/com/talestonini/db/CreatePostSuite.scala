package com.talestonini.db

import cats.effect.{IO, SyncIO}
import com.talestonini.db.model._
import com.talestonini.utils._
import java.time.ZonedDateTime
import munit.CatsEffectSuite

// make suite abstract to ignore running
// https://scalameta.org/munit/docs/filtering.html#ignore-entire-test-suite
abstract class CreatePostSuite extends CatsEffectSuite {

  var testToken: Option[String] = None
  def getTestToken()            = testToken.getOrElse(throw new Exception("test token is invalid"))

  test("get an auth token") {
    CloudFirestore.getAuthToken() flatMap { token =>
      testToken = Some(token)
      IO(assertEquals(token.substring(0, 5), "eyJhb"))
    }
  }

  val scalaDecoratorsPostsPath = "projects/ttdotcom/databases/(default)/documents/posts"
  val newPostPath              = s"$scalaDecoratorsPostsPath/${randomAlphaNumericString(20)}"

  val resource         = "dbLayerRefactor"
  val title            = "Refactoring the database access layer"
  val firstPublishDate = ZonedDateTime.now()
  val tags             = Array(Tag("aaa"), Tag("bbb"))

  test("create a post") {
    val newPost =
      Post(Some(resource), Some(title), Some(firstPublishDate), Some(firstPublishDate), Some(tags),
        Some(EnabledFor.Prod))

    CloudFirestore.upsertDocument[Post](getTestToken(), newPostPath, newPost) flatMap { post =>
      IO(assertEquals(post.fields.resource.get, resource))
      IO(assertEquals(post.fields.title.get, title))
    }
  }

}
