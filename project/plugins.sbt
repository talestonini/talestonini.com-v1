// Test Setup (must be before sbt-scalajs)
libraryDependencies += "org.scala-js" %% "scalajs-env-selenium" % "1.1.1"
// below is not happy with latest 4.11.0 (22 Aug 2023, currently porting app to Laminar)
libraryDependencies += "org.seleniumhq.selenium" % "selenium-firefox-driver" % "4.1.3"

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.13.2")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")
addSbtPlugin("org.planet42" % "laika-sbt" % "0.19.3")
