package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.App.*

object Logo {

  def apply(): Element =
    div(
      className := "w3-col w3-left logo",
      styleAttr := "width: 245px",
      a(
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
        ),
        // uncomment if willing to change the logo click behaviour to the default
        // navigateTo(HomePage)
        navigateTo(TagsPage, Some(() => Menu.initWordCloud()))
      )
    )

  private def pronunciationTd(p: String): Element =
    td(
      className := "pronunciation",
      a(
        href   := "https://www.oxfordlearnersdictionaries.com/about/english/pronunciation_english",
        target := "_blank",
        p
      )
    )

}
