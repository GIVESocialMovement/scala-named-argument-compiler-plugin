
lazy val plugin = RootProject(file("../"))

lazy val root =  (project in file("."))
  .aggregate(plugin)

name := "test-project"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.8"

organization := "givers.scala"

autoCompilerPlugins := true

//addCompilerPlugin("givers.scala.namedargument" %% "scala-named-argument-compiler-plugin" % "0.1.1-SNAPSHOT")

mainClass := Some("givers.scala.namedargument.test.Test")

scalacOptions ++= Seq(
  "-P:named-argument:annotation:givers.scala.namedargument.test.ForceNamedArgument"
)

// The below triggers `plugin` to package its jar and return the jar info.
scalacOptions in Compile ++= {
  val jar = (Keys.`package` in (plugin, Compile)).value
  System.setProperty("sbt.paths.plugin.jar", jar.getAbsolutePath)

  val addPlugin = "-Xplugin:" + jar.getAbsolutePath
  // Thanks Jason for this cool idea (taken from https://github.com/retronym/boxer)
  // add plugin timestamp to compiler options to trigger recompile of
  // main after editing the plugin. (Otherwise a 'clean' is needed in the current project)
  val dummy = "-Jdummy=" + jar.lastModified
  Seq(addPlugin, dummy)
}