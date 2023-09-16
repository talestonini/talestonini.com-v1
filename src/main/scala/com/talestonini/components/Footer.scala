package com.talestonini.components

import com.raquo.laminar.api.features.unitArrows
import com.raquo.laminar.api.L.{*, given}
import com.talestonini.{BuildInfo, Firebase}
import java.time.Year

object Footer {

  def apply(): Element =
    div(
      div(
        className := "w3-xlarge",
        iconAnchor("https://github.com/talestonini", "fa-github", "GitHub"),
        iconAnchor("https://au.linkedin.com/in/talestonini", "fa-linkedin", "LinkedIn"),
        iconAnchor("https://mastodon.world/@talestonini", "fa-mastodon", "Mastodon"),
        iconAnchor("mailto:talestonini@gmail.com", "fa-envelope", "Email")
      ),
      div(className := "w3-small", p(footerText()))
    )

  private def iconAnchor(iconHref: String, iconClass: String, eventTarget: String): Element =
    a(
      href      := iconHref,
      className := "footer-icon",
      target    := "_blank",
      i(className := s"fa $iconClass w3-hover-opacity"),
      onClick --> Firebase.gaClickedFooter(eventTarget)
    )

  private def footerText(): String =
    s"© Tales Tonini, 2019-${Year.now().getValue()} \u2014 v${BuildInfo.version}"

}
