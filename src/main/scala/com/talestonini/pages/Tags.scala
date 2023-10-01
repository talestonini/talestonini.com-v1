package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}

object Tags {

  def apply(): Element =
    div(
      div(
        idAttr    := "tags",
        className := "w3-center",
        div(idAttr := "word-cloud")
      )
    )

}
