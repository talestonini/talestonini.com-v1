package com.talestonini

import com.raquo.laminar.api.L.{*, given}
import scala.scalajs.js
import js.annotation.JSImport
import scala.xml.Elem

object WordCloudJS {

  @js.native @JSImport("word-cloud", "drawWordCloud")
  def drawWordCloud(words: js.Array[String]): Unit = js.native

  def apply(words: List[String]): Unit =
    drawWordCloud(js.Array(words: _*))

}
