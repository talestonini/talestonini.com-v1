package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.TagsJS

object Tags {

  def apply(): Element =
    div(TagsJS())

}
