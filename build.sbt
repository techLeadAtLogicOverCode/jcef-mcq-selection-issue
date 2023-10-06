lazy val jcefUserSelection =
  project.in(file("."))
    .enablePlugins(SbtIdeaPlugin)
    .settings(
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.13.10",
      ThisBuild / intellijPluginName := "jcefUserSelection",
      ThisBuild / intellijBuild      := "231.9161.38",
      ThisBuild / intellijPlatform   := IntelliJPlatform.IdeaCommunity,
      Global    / intellijAttachSources := true,
      ThisBuild / bundleScalaLibrary := false,
      intellijPlugins += "org.intellij.scala:2023.1.23".toPlugin,
      libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"
    )
