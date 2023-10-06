package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.utils.jsDisplay

object Spinner extends LoadingStyler {

  def apply(): Element =
    div(
      className := "w3-center",
      loadingStyle((b: Boolean) => s"display: ${jsDisplay(b)}"),
      p(i(className := "w3-xxxlarge fa fa-spinner w3-spin"))
    )

}
