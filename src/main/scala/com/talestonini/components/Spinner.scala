package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.utils.jsDisplay

object Spinner {

  // --- state ---------------------------------------------------------------------------------------------------------

  // whether the app is loading some content or not;
  // this is the state that backs displaying a loading animation of some kind on the UI
  private val isLoading: Var[Boolean] = Var(false)

  private var lengthyOperationsInPlace: Set[String] = Set.empty

  // --- public --------------------------------------------------------------------------------------------------------

  def apply(): Element =
    div(
      className := "w3-center",
      styleAttr <-- isLoading.signal.map(b => s"display: ${jsDisplay(b)}"),
      p(i(className := "w3-xxxlarge fa fa-spinner w3-spin"))
    )

  def start(forOperation: String): Unit = {
    lengthyOperationsInPlace = lengthyOperationsInPlace + forOperation
    isLoading.update(_ => true)
  }

  def stop(forOperation: String): Unit = {
    lengthyOperationsInPlace = lengthyOperationsInPlace - forOperation
    if (lengthyOperationsInPlace.isEmpty)
      isLoading.update(_ => false)
  }

}
