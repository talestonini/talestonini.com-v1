package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.api.features.unitArrows
import com.talestonini.db.model.Tag
import org.scalajs.dom

object Tags {

  val selectedTags: Var[Set[Tag]] = Var(Set.empty)

  def apply(): Element = {
    val pasteObserver = Observer[dom.ClipboardEvent](onNext = ev => {
      val newTag = Tag(ev.clipboardData.getData("text/plain"))
      selectedTags.update(ts => {
        if (ts.contains(newTag))
          ts - newTag
        else
          ts + newTag
      })
    })

    val clipboard = div(
      idAttr := "clipboardDiv",
      onPaste --> pasteObserver
    )

    div(
      clipboard,
      div(
        idAttr    := "tags",
        className := "w3-center",
        div(idAttr := "word-cloud")
      ),
      div(
        i(styleAttr := "font-size: 15px", "Select / unselect tags to filter the list of posts:"),
        child <-- selectedTags.signal.map(ts => div(ts.toSeq.map(tag)*))
      ),
      child <-- selectedTags.signal.map(ts => Posts(Some(ts.toSeq)))
    )
  }

  def wordCloudElementSelector(): String =
    "#tags #word-cloud"

  private def tag(t: Tag): Element =
    span(
      className := "tag w3-hover-opacity",
      styleAttr := "text-decoration: none",
      span(className := s"icon fa fa-tag"),
      t.tag,
      onClick --> selectedTags.update(ts => ts.filterNot(aTag => aTag == t))
    )

}
