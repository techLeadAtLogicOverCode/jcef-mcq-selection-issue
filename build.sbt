lazy val jcefUserSelection =
  project.in(file("."))
    .enablePlugins(SbtIdeaPlugin)
    .settings(
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.13.7",
      ThisBuild / intellijPluginName := "jcefUserSelection",
      ThisBuild / intellijBuild      := "213.5744.223",
      ThisBuild / intellijPlatform   := IntelliJPlatform.IdeaCommunity,
      Global    / intellijAttachSources := true,
      ThisBuild / bundleScalaLibrary := false,
      intellijPlugins += "org.intellij.scala:2021.3.18".toPlugin,
      libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"
    )
