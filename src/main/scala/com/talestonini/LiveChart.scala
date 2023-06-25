package com.talestonini

import com.raquo.laminar.api.L.{*, given}

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom
import com.talestonini.Main.appElement
import com.talestonini.Main.renderDataItem

// import javascriptLogo from "/javascript.svg"
@js.native @JSImport("/javascript.svg", JSImport.Default)
val javascriptLogo: String = js.native

@main
def LiveChart(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main:
  val model = new Model
  import model.*

  def appElement(): Element =
    div(
      h1("Live Chart"),
      renderDataTable()
    )
  end appElement

  def renderDataTable(): Element =
    table(
      thead(tr(th("Label"), th("Price"), th("Count"), th("Full price"), th("Action"))),
      tbody(
        children <-- dataSignal.map(data =>
          data.map { item =>
            renderDataItem(item.id, item)
          })
      ),
      tfoot(tr(
          td(button("➕", onClick --> (_ => addDataItem(DataItem())))),
          td(),
          td(),
          td(child.text <-- dataSignal.map(data => "%.2f".format(data.map(_.fullPrice).sum)))
        ))
    )
  end renderDataTable

  def renderDataItem(id: DataItemID, item: DataItem): Element =
    tr(
      td(item.label),
      td(item.price),
      td(item.count),
      td("%.2f".format(item.fullPrice)),
      td(button("🗑️", onClick --> (_ => removeDataItem(id))))
    )
  end renderDataItem
end Main
