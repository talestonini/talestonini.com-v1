package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.modifiers.KeyUpdater.HtmlAttrUpdater
import com.raquo.laminar.keys.HtmlAttr

trait LoadingStyler {

  // --- state ---------------------------------------------------------------------------------------------------------

  // whether the app is loading some content or not;
  // this is the state that backs displaying a loading animation of some kind on the UI
  private val isLoading: Var[Boolean] = Var(false)

  private var lengthyOperationsInPlace: Set[String] = Set.empty

  // --- UI ------------------------------------------------------------------------------------------------------------

  def loadingStyle(loadingExpr: Boolean => String): HtmlAttrUpdater[String] =
    styleAttr <-- isLoading.signal.map(b => loadingExpr(b))

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
