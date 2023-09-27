package com.talestonini

import com.raquo.laminar.api.L.{*, given}
import scala.scalajs.js
import js.annotation.JSImport
import scala.xml.Elem

object TagsJS {

  @js.native @JSImport("tags", "d3Content")
  def d3Content(): Elem = js.native

  def apply(): Element = {
    val element = div()
    element.ref.innerHTML = d3Content().toString
    element
  }

}
