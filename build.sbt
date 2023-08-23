import org.scalajs.linker.interface.ModuleSplitStyle

val circeVersion  = "0.15.0-M1"
val http4sVersion = "1.0.0-M32" // proving hard to upgrade this dependency (23 Aug 2023)

lazy val ttDotCom = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .enablePlugins(BuildInfoPlugin)
  .settings(
    scalaVersion := "3.3.0",

    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "com.talestonini" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("com.talestonini"))
        )
    },

    // BuilfInfoPlugin
    buildInfoKeys    := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.talestonini",

    // Dependencies
    libraryDependencies ++= Seq(
      /* Depend on the scalajs-dom library.
       * It provides static types for the browser DOM APIs.
       */
      "org.scala-js" %%% "scalajs-dom" % "2.6.0",

      // Depend on Laminar
      "com.raquo" %%% "laminar"  % "16.0.0",
      "com.raquo" %%% "waypoint" % "7.0.0", // routing, independent of Laminar

      // Http4s
      "io.circe"   %%% "circe-core"      % circeVersion,
      "io.circe"   %%% "circe-generic"   % circeVersion,
      "io.circe"   %%% "circe-parser"    % circeVersion,
      "org.http4s" %%% "http4s-circe"    % http4sVersion,
      "org.http4s" %%% "http4s-client"   % http4sVersion,
      "org.http4s" %%% "http4s-dom"      % http4sVersion,
      "io.monix"   %%% "monix-execution" % "3.4.1",

      // Java Time for Scala.js
      "io.github.cquiroz" %%% "scala-java-time"      % "2.5.0",
      "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.5.0",

      // Testing framework
      "org.scalameta" %%% "munit"               % "1.0.0-M8" % Test,
      "org.scalatest" %%% "scalatest"           % "3.3.0-SNAP4" % Test,
      "org.typelevel" %%% "munit-cats-effect-3" % "1.0.7"    % Test
    )
  )

// Test setup
Test / jsEnv := new org.scalajs.jsenv.selenium.SeleniumJSEnv(new org.openqa.selenium.firefox.FirefoxOptions())
