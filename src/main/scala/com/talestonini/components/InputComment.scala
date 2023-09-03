package com.talestonini.components

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.utils.javascript.jsDisplay

object InputComment {

  // --- data ----------------------------------------------------------------------------------------------------------

  private val isInputting = Var(false)

  private val initComment = "What do you think?"
  private val comment     = Var(initComment)
  private val rowCount    = Var(1)

  private val initName = "Name"
  private val name     = Var(initName)

  // --- public --------------------------------------------------------------------------------------------------------

  def apply(persistCommentFn: (author: String, comment: String) => Unit): Element = {
    def commentButtonHandler() = {
      if (comment.signal.now().nonEmpty) {
        persistCommentFn(name.signal.now(), comment.signal.now())
        clearInputs()
      }
    }

    def clearInputs() = {
      comment.update(_ => initComment)
      rowCount.update(_ => 1)
      name.update(_ => initName)
      isInputting.update(_ => false)
    }

    div(
      className := "w3-panel w3-light-grey w3-leftbar w3-padding-16",
      commentTextArea(),
      div(
        className := "w3-padding-8",
        styleAttr <-- isInputting.signal.map(b => s"display: ${jsDisplay(b)}"),
        nameInputText()
      ),
      div(
        className := "w3-right",
        styleAttr <-- isInputting.signal.map(b => s"display: ${jsDisplay(b)}"),
        div(
          className := "w3-bar",
          aButton("Comment", commentButtonHandler),
          aButton("Cancel", clearInputs)
        )
      )
    )
  }

  // --- private -------------------------------------------------------------------------------------------------------

  private def commentTextArea(): Element = {
    def onFocusHandler() = {
      comment.update(_ => "")
      rowCount.update(_ => 5)
      isInputting.update(_ => true)
    }

    textArea(
      className   := "w3-input w3-border",
      placeholder := initComment,
      rows <-- rowCount,
      value <-- comment.signal,
      onFocus --> (_ => onFocusHandler()),
      onInput.mapToValue --> (newComment => comment.update(_ => newComment))
    )
  }

  private def nameInputText(): Element =
    input(
      className   := "w3-input w3-border",
      typ         := "text",
      placeholder := initName,
      value <-- name.signal,
      onFocus --> (_ => name.update(_ => "")),
      onInput.mapToValue --> (newName => name.update(_ => newName))
    )

  private def aButton(caption: String, handlerFn: () => Unit): Element = {
    val buttonClasses = "w3-button w3-ripple w3-padding w3-black"
    button(typ := "button", className := buttonClasses, onClick --> (_ => handlerFn()), caption)
  }

}
