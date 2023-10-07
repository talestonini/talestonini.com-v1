package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.db.model.*
import com.talestonini.App.navigateByPostResource
import com.talestonini.utils.datetime2Str

object Posts {

  val posts: Var[Docs[Post]] = Var(Seq.empty)

  def apply(filter: Option[Seq[Tag]] = None): Element =
    div(
      className := "post-list",
      children <-- buildPostLinks(filter.getOrElse(Seq.empty))
    )

  private def buildPostLinks(filter: Seq[Tag]): Signal[Seq[Element]] =
    posts.signal.map { ps =>
      for {
        post <- ps
        fields = post.fields
        if fields.tags.map(tags => filter.toSet.subsetOf(tags.toSet)).getOrElse(true)
      } yield div(
        p(
          div(
            className := "post-date",
            a(
              navigateByPostResource(fields.resource.get),
              i(datetime2Str(fields.publishDate)),
              firstPublishDate(fields)
            )
          ),
          a(
            className := "w3-bold",
            navigateByPostResource(fields.resource.get),
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
        s" (first ${datetime2Str(fields.firstPublishDate)})"
      )

}
