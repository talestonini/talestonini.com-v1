package com.talestonini

import com.raquo.laminar.api.L.{*, given}

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom
import com.talestonini.components.{Logo, Menu, Footer}

@main
def TalesToniniDotCom(): Unit =
  // bootstraps Laminar by installing a Laminar Element in an existing DOM element
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main:
  val model = new Model
  import model.*

  def appElement(): Element =
    div(
      div(className("w3-content w3-row w3-hide-small"), div(className("w3-padding-16"), Logo(), Menu()), hr()),
      div(className("w3-content w3-row w3-hide-large w3-hide-medium"),
        div(className("w3-padding-8"), Logo(), Menu(isMobile = true)), hr()),
      div(className("footer w3-container w3-padding-16 w3-center w3-hide-small"), Footer()),
      div(className("footer w3-container w3-padding-16 w3-center w3-hide-large w3-hide-medium"), Footer())
    )
  end appElement
end Main
