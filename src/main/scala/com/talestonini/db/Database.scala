package com.talestonini.db

import cats.Monad
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.talestonini.db.model._
import io.circe.{Encoder, Decoder}

trait Database[M[_]] {

  def getAuthToken(): M[String]

  def getDocuments[T <: Model](token: String, path: String)(
    implicit docsResDecoder: Decoder[DocsRes[T]]
  ): M[Docs[T]]

  def upsertDocument[T <: Model](token: String, path: String, model: T)(
    implicit docDecoder: Decoder[Doc[T]], bodyEncoder: Encoder[Body[T]]
  ): M[Doc[T]]

  def deleteDocument[T <: Model](token: String, path: String)(
    implicit docDecoder: Decoder[Doc[T]]
  ): M[Option[Throwable]]

  // -------------------------------------------------------------------------------------------------------------------

  def getDocuments[T <: Model, M[_]: Monad](db: Database[M], path: String)(
    implicit docsResDecoder: Decoder[DocsRes[T]]
  ): M[Docs[T]] =
    for {
      token <- db.getAuthToken()
      docs  <- db.getDocuments(token, path)
    } yield docs

  def upsertDocument[T <: Model, M[_]: Monad](db: Database[M], path: String, model: T)(
    implicit docDecoder: Decoder[Doc[T]], bodyEncoder: Encoder[Body[T]]
  ): M[Doc[T]] =
    for {
      token <- db.getAuthToken()
      doc   <- db.upsertDocument(token, path, model)
    } yield doc

  def deleteDocument[T <: Model, M[_]: Monad](db: Database[M], path: String)(
    implicit docDecoder: Decoder[Doc[T]]
  ): M[Option[Throwable]] =
    for {
      token <- db.getAuthToken()
      t     <- db.deleteDocument(token, path)
    } yield t

}
