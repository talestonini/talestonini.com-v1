package com.talestonini.db

import cats.effect.IO
import com.talestonini.db.model._
import com.talestonini.utils.randomAlphaNumericString
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{Headers, Header, Method, Request}
import org.http4s.circe._
import org.http4s.dom.FetchClientBuilder
import org.http4s.implicits._
import org.http4s.{Uri, UriTemplate}
import org.http4s.UriTemplate._
import org.typelevel.ci._

object CloudFirestore extends Database[IO] {

  private val ApiKey        = "AIzaSyDSpyLoxb_xSC7XAO-VUDJ0Hd_XyuquAnY" // restricted web app API key
  private val ProjectId     = "ttdotcom"
  private val Database      = "(default)"
  private val FirestoreHost = "firestore.googleapis.com"

  case class CloudFirestoreException(msg: String) extends Exception(msg)

  def getPosts(token: String): IO[Docs[Post]] =
    getDocuments[Post](token, s"projects/$ProjectId/databases/$Database/documents/posts")

  def getPosts(): IO[Docs[Post]] =
    getDocuments[Post, IO](this, s"projects/$ProjectId/databases/$Database/documents/posts")

  def getComments(token: String, postDocName: String): IO[Docs[Comment]] =
    getDocuments[Comment](token, s"$postDocName/comments")

  def getComments(postDocName: String): IO[Docs[Comment]] =
    getDocuments[Comment, IO](this, s"$postDocName/comments")

  def createComment(token: String, postDocName: String, comment: Comment): IO[Doc[Comment]] = {
    val newCommentId   = randomAlphaNumericString(20)
    val commentDocName = s"$postDocName/comments/$newCommentId"
    upsertDocument[Comment](token, commentDocName, comment)
  }

  def createComment(postDocName: String, comment: Comment): IO[Doc[Comment]] = {
    val newCommentId   = randomAlphaNumericString(20)
    val commentDocName = s"$postDocName/comments/$newCommentId"
    upsertDocument[Comment, IO](this, commentDocName, comment)
  }

  def removeComment(token: String, path: String): IO[Option[Throwable]] =
    deleteDocument[Comment](token, path)

  def removeComment(path: String): IO[Option[Throwable]] =
    deleteDocument[Comment, IO](this, path)

  // -------------------------------------------------------------------------------------------------------------------

  def getAuthToken(): IO[String] = {
    val uri     = uri"https://identitytoolkit.googleapis.com/v1/accounts:signUp".withQueryParam("key", ApiKey)
    val request = Request[IO](Method.POST, uri).withHeaders(Headers(Header.Raw(ci"Content-Type", "application/json")))

    FetchClientBuilder[IO].create
      .expectOr[AuthTokenResponse](request)(response =>
        IO(CloudFirestoreException(s"failed requesting signUp token: $response")))
      .map(response => response.idToken)
  }

  def getDocuments[T <: Model](token: String,
    path: String)(
    implicit docsResDecoder: Decoder[DocsRes[T]]
  ): IO[Docs[T]] = {
    val uri     = toFirestoreUri(path)
    val request = Request[IO](Method.GET, uri).withHeaders(Header.Raw(CIString("Authorization"), s"Bearer $token"))

    FetchClientBuilder[IO].create
      .expectOr[DocsRes[T]](request)(response => IO(CloudFirestoreException(s"failed getting documents: $response")))
      .map(docsRes => docsRes.documents.sortBy(_.fields.sortingField).reverse)
  }

  def upsertDocument[T <: Model](token: String, path: String,
    model: T)(
    implicit docDecoder: Decoder[Doc[T]], bodyEncoder: Encoder[Body[T]]
  ): IO[Doc[T]] =
    if (isBadRequest(model.content)) {
      IO.raiseError(new Exception(s"bad request")) // anti hack protection
    } else {
      val uri = toFirestoreUri(path).withQueryParam("updateMask.fieldPaths", model.dbFields)
      val request = Request[IO](Method.PATCH, uri)
        .withEntity[Json](Body(path, model).asJson)
        .withHeaders(Header.Raw(CIString("Authorization"), s"Bearer $token"))

      FetchClientBuilder[IO].create
        .expectOr[Doc[T]](request)(response => IO(CloudFirestoreException(s"failed upserting document: $response")))
    }

  def deleteDocument[T <: Model](token: String,
    path: String)(
    implicit docDecoder: Decoder[Doc[T]]
  ): IO[Option[Throwable]] = {
    val uri = toFirestoreUri(path)
    val request = Request[IO](method = Method.DELETE, uri = uri)
      .withHeaders(Header.Raw(CIString("Authorization"), s"Bearer $token"))

    FetchClientBuilder[IO].create
      .successful(request)
      .map {
        case true  => None
        case false => Some(CloudFirestoreException("failed deleting document"))
      }
  }

  private def toFirestoreUri(path: String): Uri =
    UriTemplate(
      authority = Some(Uri.Authority(host = Uri.RegName(FirestoreHost))),
      scheme = Some(Uri.Scheme.https),
      path = List(PathElm("v1"), PathElm(path))
    ).toUriIfPossible.getOrElse(throw CloudFirestoreException("unable to build URI"))

  // map of session -> (last usage time, last usage content)
  private var antiHackCache: Map[String, (Long, String)] = Map.empty

  private def isBadRequest(content: String): Boolean = {
    val now                 = java.lang.System.currentTimeMillis()
    val (luTime, luContent) = antiHackCache.get(session).getOrElse((0L, ""))

    // update the cache
    antiHackCache = antiHackCache.updated(session, (now, content))

    val isBadInterval    = luTime > 0L && (now - luTime) < 1000
    val isSimilarContent = luContent.nonEmpty && content.toSeq.diff(luContent).unwrap.length() < 3
    isBadInterval || isSimilarContent
  }

  // --- private -------------------------------------------------------------------------------------------------------

  private lazy val session = randomAlphaNumericString(30)

}
