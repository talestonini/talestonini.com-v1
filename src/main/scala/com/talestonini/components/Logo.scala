package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.codecs.StringAsIsCodec

object Logo {

  private val pronunciationUrl = "https://www.oxfordlearnersdictionaries.com/about/english/pronunciation_english"

  def apply(): Element =
    div(
      className := "w3-col w3-left logo",
      styleAttr := "width: 245px",
      a(
        href := "#/",
        table(
          tr(
            td(className := "symbol", "❯"),
            td(className := "tales_t", "T"),
            td(className := "ales", "ales"),
            pronunciationTd("/tɑː \u2022 les/")
          ),
          tr(
            td(),
            td(className := "tonini_t", "T"),
            td(className := "onini", "onini"),
            pronunciationTd("/toʊ \u2022 niː \u2022 nɪ/")
          ),
          tr(
            td(),
            td(className := "dot", "•"),
            td(className := "com", "com"),
            td()
          )
        )
      )
    )

  private def pronunciationTd(p: String): Element =
    td(
      className := "pronunciation",
      a(href := pronunciationUrl, target := "_blank", p)
    )

}
