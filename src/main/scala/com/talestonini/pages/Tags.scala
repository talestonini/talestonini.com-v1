package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}

object Tags {

  def apply(): Element =
    div(idAttr := "tags", className := "w3-center")

}
