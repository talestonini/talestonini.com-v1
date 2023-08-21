package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.codecs.StringAsIsCodec
import com.talestonini.BuildInfo
import java.time.Year

object Footer {

  def apply(): Element =
    div(
      div(className("w3-xlarge"),
        a(href("https://au.linkedin.com/in/talestonini"), className("footer-icon"), target("_blank"),
          i(className("fa fa-linkedin w3-hover-opacity"))),
        a(href("https://github.com/talestonini"), className("footer-icon"), target("_blank"),
          i(className("fa fa-github w3-hover-opacity"))),
        a(href("mailto:talestonini@gmail.com"), className("footer-icon"), target("_blank"),
          i(className("fa fa-envelope w3-hover-opacity")))),
      div(className("w3-small"), p(footerText()))
    )

  private def footerText(): String =
    s"© Tales Tonini, 2019-${Year.now().getValue()} \u2014 v${BuildInfo.version}"

}
