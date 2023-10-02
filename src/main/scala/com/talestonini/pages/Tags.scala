package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object Tags {

  def apply(): Element = {
    val pasteObserver = Observer[dom.ClipboardEvent](onNext = ev => {
      println(">>> got last clicked tag: " + ev.clipboardData.getData("text/plain"))
    })

    val clipboard = div(
      idAttr := "clipboardDiv",
      onPaste --> pasteObserver
    )

    div(
      clipboard,
      div(
        idAttr    := "tags",
        className := "w3-center",
        div(idAttr := "word-cloud")
      )
    )
  }

  def wordCloudElementSelector(): String =
    "#tags #word-cloud"

}
