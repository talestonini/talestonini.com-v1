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
    // this is what many UI frameworks call a "component" - input is a model value, output is a UI element that
    // can manipulate this value; it is simply a method in Laminar
    def inputForString(valueSignal: Signal[String], valueUpdater: Observer[String]): Input =
      input(typ := "text", value <-- valueSignal, onInput.mapToValue --> valueUpdater)

    def makeDataItemUpdater[A](id: DataItemID, f: (DataItem, A) => DataItem): Observer[A] =
      dataVar.updater { (data, newValue) =>
        data.map(i => if i.id == id then f(i, newValue) else i)
      }

    def inputForDouble(valueSignal: Signal[Double], valueUpdater: Observer[Double]): Input =
      val strValue = Var[String]("")
      input(
        typ := "text",
        value <-- strValue.signal,
        onInput.mapToValue --> strValue,
        valueSignal --> strValue.updater[Double] { (prevStr, newValue) =>
          if prevStr.toDoubleOption.contains(newValue) then prevStr
          else "%.2f".format(newValue)
        },
        strValue.signal --> { valueStr =>
          valueStr.toDoubleOption.foreach(valueUpdater.onNext)
        }
      )
    end inputForDouble

    def inputForInt(valueSignal: Signal[Int], valueUpdater: Observer[Int]): Input =
      input(
        typ := "text",
        controlled(
          value <-- valueSignal.map(_.toString()),
          onInput.mapToValue.map(_.toIntOption).collect { case Some(newCount) =>
            newCount
          } --> valueUpdater
        )
      )

    tr(
      td(inputForString(itemSignal.map(_.label), makeDataItemUpdater(id, (i, newLabel) => i.copy(label = newLabel)))),
      td(inputForDouble(itemSignal.map(_.price), makeDataItemUpdater(id, (i, newPrice) => i.copy(price = newPrice)))),
      td(inputForInt(itemSignal.map(_.count), makeDataItemUpdater(id, (i, newCount) => i.copy(count = newCount)))),
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
