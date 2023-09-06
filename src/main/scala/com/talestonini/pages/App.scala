package com.talestonini.pages

import com.raquo.laminar.api.L.{*, given}
import com.talestonini.components.{Logo, Menu, Footer, Spinner}

import com.raquo.laminar.api.*
import com.raquo.waypoint.*
import com.talestonini.pages.*
import com.talestonini.pages.sourcegen.*
import com.talestonini.pages.sourcegen.posts.*
import org.scalajs.dom
import upickle.default.*
import upickle.default.ReadWriter.join

sealed trait Page derives ReadWriter
case object DbLayerRefactorPage extends Page
case object PostsPage           extends Page
case object ScalaDecoratorsPage extends Page

val dbLayerRefactorRoute = Route.static(DbLayerRefactorPage, root / "dbLayerRefactor" / endOfSegments)
val postsRoute           = Route.static(PostsPage, root / "posts" / endOfSegments)
val scalaDecoratorsRoute = Route.static(ScalaDecoratorsPage, root / "scalaDecorators" / endOfSegments)

val router = new Router[Page](
  routes = List(
    dbLayerRefactorRoute,
    postsRoute,
    scalaDecoratorsRoute
  ),
  getPageTitle = _.toString,                 // mock page title (displayed in the browser tab next to favicon)
  serializePage = page => write(page),       // serialize page data for storage in History API log
  deserializePage = pageStr => read(pageStr) // deserialize the above
)(
  popStateEvents = L.windowEvents(_.onPopState), // this is how Waypoint avoids an explicit dependency on Laminar
  owner = L.unsafeWindowOwner                    // this router will live as long as the window
)

def renderPage(page: Page): Element = {
  page match {
    case DbLayerRefactorPage => DbLayerRefactor()
    case PostsPage           => Posts()
    case ScalaDecoratorsPage => ScalaDecorators()
  }
}

object App {

  def apply(): Element =
    div(
      div(
        className := "w3-content w3-row w3-hide-small",
        div(
          className := "w3-padding-16",
          Logo(),
          Menu()
        ),
        hr()
      ),
      div(
        className := "w3-content w3-row w3-hide-large w3-hide-medium",
        div(
          className := "w3-padding-8",
          Logo(),
          Menu(isMobile = true)
        ),
        hr()
      ),
      div(
        className := "w3-content",
        div(
          className := "content w3-padding-16",
          Spinner(),
          child <-- router.currentPageSignal.map(renderPage)
        ),
        hr()
      ),
      div(
        className := "footer w3-container w3-padding-16 w3-center w3-hide-small",
        Footer()
      ),
      div(
        className := "footer w3-container w3-padding-16 w3-center w3-hide-large w3-hide-medium",
        Footer()
      )
    )

}
