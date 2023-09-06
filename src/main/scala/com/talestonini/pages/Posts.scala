package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.db.model.*
import com.talestonini.utils.datetime2Str

object Posts {

  val posts: Var[Docs[Post]] = Var(Seq.empty)

  def apply(): Element =
    div(
      className := "post-list",
      children <-- buildPostLinks()
    )

  private def buildPostLinks(): Signal[Seq[Element]] =
    posts.signal.map { ps =>
      for {
        post <- ps
        fields = post.fields
      } yield div(
        p(
          div(
            className := "post-date",
            a(
              href := s"#/${fields.resource}",
              i(datetime2Str(fields.publishDate)),
              firstPublishDate(fields)
            )
          ),
          a(
            className := "w3-bold",
            href      := s"#/${fields.resource}",
            fields.title
          )
        )
      )
    }

  private def firstPublishDate(fields: Post): Element =
    if (datetime2Str(fields.firstPublishDate) == datetime2Str(fields.publishDate))
      span()
    else
      i(
        className := "first-published",
        " (first ",
        datetime2Str(fields.firstPublishDate),
        ")"
      )

}
