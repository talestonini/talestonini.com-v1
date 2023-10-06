package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}

object LoadingHr extends LoadingStyler {

  def apply(): Element =
    hr(
      loadingStyle((b: Boolean) => s"animation: slideAnimation ${if b then 5 else 0}s infinite linear")
    )

}
