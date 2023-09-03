package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.codecs.StringAsIsCodec
import com.talestonini.BuildInfo
import java.time.Year

object Footer {

  def apply(): Element =
    div(
      div(
        className := "w3-xlarge",
        iconAnchor("https://au.linkedin.com/in/talestonini", "fa-linkedin"),
        iconAnchor("https://github.com/talestonini", "fa-github"),
        iconAnchor("mailto:talestonini@gmail.com", "fa-envelope")
      ),
      div(className := "w3-small", p(footerText()))
    )

  private def iconAnchor(iconHref: String, iconClass: String): Element =
    a(
      href      := iconHref,
      className := "footer-icon",
      target    := "_blank",
      i(className := s"fa $iconClass w3-hover-opacity")
    )

  private def footerText(): String =
    s"© Tales Tonini, 2019-${Year.now().getValue()} \u2014 v${BuildInfo.version}"

}
