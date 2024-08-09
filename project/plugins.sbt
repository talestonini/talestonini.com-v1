// Test Setup (must be before sbt-scalajs)
libraryDependencies += "org.scala-js" %% "scalajs-env-selenium" % "1.1.1"
libraryDependencies += "io.github.gmkumar2005" %% "scala-js-env-playwright" % "0.1.11"

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.13.2")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")
addSbtPlugin("org.planet42" % "laika-sbt" % "0.19.3")
