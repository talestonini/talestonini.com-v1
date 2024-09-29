import org.scalajs.linker.interface.ModuleSplitStyle
import sbt.internal.util.ManagedLogger

val scalaVer  = "3.5.1"     // update prep_public.sh to match this version
val circeVer  = "0.14.10"
val http4sVer = "1.0.0-M32" // proving hard to upgrade this dependency (23 Aug 2023)

lazy val ttDotCom = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .enablePlugins(BuildInfoPlugin, LaikaPlugin)
  .settings(
    scalaVersion := s"$scalaVer",
    scalacOptions ++= Seq(
      "-deprecation", // emit warning and location for usages of deprecated APIs
      "-feature",     // emit warning and location for usages of features that should be imported explicitly
      "-unchecked"    // enable additional warnings where generated code depends on assumptions
    ),
    version := "1.1.11",

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
      "org.scala-js" %%% "scalajs-dom" % "2.8.0",

      // Depend on Laminar (reactive web application pages and routing)
      "com.raquo" %%% "laminar"  % "17.1.0",
      "com.raquo" %%% "waypoint" % "8.0.1", // routing, independent of Laminar

      // For serialising page data for storage in History API log
      // (used by the Wayoint routing code)
      "com.lihaoyi" %%% "upickle" % "4.0.2",

      // XML (for the pages with content generated by Laika)
      "org.scala-lang.modules" %%% "scala-xml" % "2.3.0",

      // Http4s (backend and database stuff)
      "io.circe"   %%% "circe-core"      % circeVer,
      "io.circe"   %%% "circe-generic"   % circeVer,
      "io.circe"   %%% "circe-parser"    % circeVer,
      "org.http4s" %%% "http4s-circe"    % http4sVer,
      "org.http4s" %%% "http4s-client"   % http4sVer,
      "org.http4s" %%% "http4s-dom"      % http4sVer,
      "io.monix"   %%% "monix-execution" % "3.4.1",

      // Java Time for Scala.js
      "io.github.cquiroz" %%% "scala-java-time"      % "2.6.0",
      "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.6.0",

      // Testing framework
      "org.scalameta" %%% "munit"             % "1.0.2"         % Test,
      "org.scalatest" %%% "scalatest"         % "3.3.0-alpha.1" % Test,
      "org.typelevel" %%% "munit-cats-effect" % "2.0.0"         % Test
    )
  )

// ---------------------------------------------------------------------------------------------------------------------
// Test setup
// ---------------------------------------------------------------------------------------------------------------------
Test / jsEnv := new jsenv.playwright.PWEnv(browserName = "chrome", headless = true, showLogs = true)
Test / test  := ((Test / test) dependsOn replaceTestSecrets dependsOn (Test / fastLinkJS)).value

// ---------------------------------------------------------------------------------------------------------------------
// Firebase setup
// Tasks fastLinkJS and fullLinkJS to replace code secrets from files .secrets-dev and .secrets-prod
// ---------------------------------------------------------------------------------------------------------------------
lazy val replaceDevSecrets = taskKey[Unit]("Replaces secret references in the code for fast linking")
replaceDevSecrets := {
  val log = streams.value.log
  log.info("Replacing DEV secret references:")
  loadSecretsFrom(baseDirectory.value / ".secrets-dev").foreach { entry =>
    replaceString(
      log,
      baseDirectory.value / s"target/scala-$scalaVer/ttdotcom-fastopt",
      "com.talestonini*.js",
      entry._1,
      entry._2
    )
  }
}

lazy val replaceTestSecrets = taskKey[Unit]("Replaces secret references in the code for test fast linking")
replaceTestSecrets := {
  val log = streams.value.log
  log.info("Replacing TEST secret references:")
  loadSecretsFrom(baseDirectory.value / ".secrets-dev").foreach { entry =>
    replaceString(
      log,
      baseDirectory.value / s"target/scala-$scalaVer/ttdotcom-test-fastopt",
      "com.talestonini*.js",
      entry._1,
      entry._2
    )
  }
}

lazy val replaceProdSecrets = taskKey[Unit]("Replaces secret references in the code for full linking")
replaceProdSecrets := {
  val log = streams.value.log
  log.info("Replacing PROD secret references:")
  loadSecretsFrom(baseDirectory.value / ".secrets-prod").foreach { entry =>
    replaceString(
      log,
      baseDirectory.value / s"target/scala-$scalaVer/ttdotcom-opt",
      "com.talestonini*.js",
      entry._1,
      entry._2
    )
  }
}

def replaceString(log: ManagedLogger, dir: File, fileFilter: String, from: String, to: String) = {
  val toReplace = s"@$from@"
  val files     = (dir ** fileFilter).get
  log.info(s"* ${files.size} files to check for secret $from")
  files.foreach { f =>
    val content = IO.read(f)
    if (content.contains(toReplace)) {
      log.info(s"* replacing $from in file ${f.name}")
      val replacement = content.replace(toReplace, to)
      IO.write(f, replacement)
    }
  }
}

def loadSecretsFrom(file: File): Seq[(String, String)] = {
  scala.io.Source
    .fromFile(file)
    .getLines()
    .map(line => {
      val entry = line.split('=').toList
      (entry.head, entry.tail.head)
    })
    .toSeq
}

fastLinkJS := (Def.taskDyn {
  val fljs = (Compile / fastLinkJS).value
  Def.task {
    val rs = replaceDevSecrets.value
    fljs
  }
}).value

fullLinkJS := (Def.taskDyn {
  val fljs = (Compile / fullLinkJS).value
  Def.task {
    val rs = replaceProdSecrets.value
    fljs
  }
}).value

// ---------------------------------------------------------------------------------------------------------------------
// LaikaPlugin setup
// Tasks to generate scala classes from MarkDown pages
// ---------------------------------------------------------------------------------------------------------------------
Laika / sourceDirectories := Seq(sourceDirectory.value / "main/resources/pages")
laikaSite / target        := sourceDirectory.value / "main/scala/com/talestonini/pages/sourcegen"
laikaTheme                := laika.theme.Theme.empty
laikaExtensions           := Seq(laika.format.Markdown.GitHubFlavor)
laikaConfig               := LaikaConfig.defaults.withRawContent

lazy val laikaHTML2Scala = taskKey[Unit]("Renames Laika's .html outputs to .scala")
laikaHTML2Scala := {
  renameHtmlToScala(sourceDirectory.value / "main/scala/com/talestonini/pages/sourcegen")
  renameHtmlToScala(sourceDirectory.value / "main/scala/com/talestonini/pages/sourcegen/posts")
}

def renameHtmlToScala(dir: File) = {
  file(dir.getAbsolutePath)
    .listFiles()
    .map(f => {
      val filename = f.getAbsolutePath()
      if (filename.endsWith("html")) {
        val prefix = filename.substring(0, filename.lastIndexOf("."))
        f.renameTo(new File(prefix + ".scala"))
      }
    })
}

lazy val laikaPrep = taskKey[Unit]("Runs all Laika-related tasks at once")
laikaPrep         := Def.sequential(laikaHTML, laikaHTML2Scala).value
Compile / compile := ((Compile / compile) dependsOn laikaPrep).value
