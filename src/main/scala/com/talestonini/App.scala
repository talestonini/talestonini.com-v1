package com.talestonini

import com.raquo.laminar.api.L.{*, given}

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom
import com.talestonini.components.Logo

@main
def main(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    App.appElement()
  )

object App:
  val model = new Model
  import model.*

  def appElement(): Element =
    div(
      Logo()
    )
  end appElement
end App
