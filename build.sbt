lazy val jcefUserSelection =
  project.in(file("."))
    .enablePlugins(SbtIdeaPlugin)
    .settings(
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.13.2",
      ThisBuild / intellijPluginName := "jcefUserSelection",
      ThisBuild / intellijBuild      := "212.4746.92",
      ThisBuild / intellijPlatform   := IntelliJPlatform.IdeaCommunity,
      Global    / intellijAttachSources := true,
      ThisBuild / bundleScalaLibrary := false,
      intellijPlugins += "org.intellij.scala:2021.2.22".toPlugin,
      libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"
    )
