package com.talestonini

import com.raquo.laminar.api.L.{*, given}

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

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
      renderDataTable(),
      renderDataList()
    )
  end appElement

  def renderDataTable(): Element =
    table(
      thead(tr(th("Label"), th("Price"), th("Count"), th("Full price"), th("Action"))),
      tbody(
        children <-- dataSignal.split(_.id) { (id, initial, itemSignal) =>
          renderDataItem(id, itemSignal)
        }
      ),
      tfoot(tr(
          td(button("➕", onClick --> (_ => addDataItem(DataItem())))),
          td(),
          td(),
          td(child.text <-- dataSignal.map(data => "%.2f".format(data.map(_.fullPrice).sum)))
        ))
    )
  end renderDataTable

  def renderDataItem(id: DataItemID, itemSignal: Signal[DataItem]): Element =
    def renderLabelInput(): Element = td(
      input(
        typ := "text",
        value <-- itemSignal.map(_.label),
        onInput.mapToValue --> { (newLabel: String) =>
          dataVar.update(data => data.map(i => if i.id == id then i.copy(label = newLabel) else i))
        }
      )
    )

    tr(
      td(renderLabelInput()),
      td(child.text <-- itemSignal.map(i => "%.2f".format(i.price))),
      td(child.text <-- itemSignal.map(_.count)),
      td(child.text <-- itemSignal.map(i => "%.2f".format(i.fullPrice))),
      td(button("🗑️", onClick --> (_ => removeDataItem(id))))
    )
  end renderDataItem

  def renderDataList(): Element =
    ul(
      children <-- dataSignal.split(_.id) { (id, initial, itemSignal) =>
        li(child.text <-- itemSignal.map(i => s"${i.count} ${i.label}"))
      }
    )
  end renderDataList
end Main
