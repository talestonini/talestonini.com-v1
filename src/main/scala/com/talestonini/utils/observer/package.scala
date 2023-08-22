package com.talestonini.utils

package object observer {

  object EventName extends Enumeration {
    type EventName = Value
    val UserSignedIn, UserSignedOut = Value
  }
  import EventName.EventName

  trait Observer {
    def onNotify(e: EventName): Unit
  }

  trait Observable {
    def register(o: Observer, es: EventName*): Unit
    def notifyObservers(e: EventName): Unit
  }

  abstract class SimpleObservable extends Observable {
    var observers: Map[EventName, Seq[Observer]] = Map.empty

    def register(o: Observer, es: EventName*): Unit =
      for (e <- es) {
        var os = observers.get(e).getOrElse(Seq.empty)
        if (os.isEmpty)
          observers = observers + (e -> Seq(o))
        else
          observers = observers + (e -> (os :+ o))
      }

    def notifyObservers(e: EventName): Unit = {
      val os = observers.get(e).getOrElse(Seq.empty)
      os.foreach(_.onNotify(e))
    }
  }

}
