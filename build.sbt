import org.scalajs.linker.interface.ModuleSplitStyle

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

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.6.0",

    // Depend on Laminar
    libraryDependencies += "com.raquo" %%% "laminar" % "16.0.0",
    libraryDependencies += "com.raquo" %%% "waypoint" % "7.0.0", // routing, independent of Laminar

    // Testing framework
    libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0-M8" % Test,

    // Java Time for Scala.js
    libraryDependencies += "io.github.cquiroz" %%% "scala-java-time"      % "2.5.0",
    libraryDependencies += "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.5.0"
  )
