package com.talestonini

import scala.scalajs.js
import js.annotation.JSImport

object WordCloud {

  @js.native @JSImport("word-cloud", "drawWordCloud")
  def drawWordCloud(whereSelector: String, words: js.Array[String]): Unit = js.native

  def apply(whereSelector: String, words: List[String]): Unit =
    drawWordCloud(whereSelector, js.Array(words*))

}
