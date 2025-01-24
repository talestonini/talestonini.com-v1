// Test Setup (must be before sbt-scalajs)
libraryDependencies += "org.scala-js"          %% "scalajs-env-selenium"    % "1.1.1"
libraryDependencies += "io.github.gmkumar2005" %% "scala-js-env-playwright" % "0.1.12"

addSbtPlugin("org.scala-js"  % "sbt-scalajs"   % "1.18.2")
addSbtPlugin("com.eed3si9n"  % "sbt-buildinfo" % "0.13.1")
addSbtPlugin("org.typelevel" % "laika-sbt"     % "1.3.1")
