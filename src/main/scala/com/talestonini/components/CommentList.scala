package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.db.model.*
import com.talestonini.utils.*

object CommentList {

  def apply(comments: Docs[Comment]): Seq[Element] =
    for (c <- comments)
      yield aComment(c.fields)

  private def aComment(c: Comment): Element =
    div(
      className := "w3-panel w3-light-grey w3-leftbar",
      p(i(c.text)),
      p(
        c.author.get.name.get,
        span(styleAttr := "padding: 0 15px 0 15px", "|"),
        i(datetime2Str(c.date))
      )
    )

}
