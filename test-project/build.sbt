
lazy val plugin = RootProject(file("../"))

lazy val root =  (project in file("."))
  .aggregate(plugin)

name := "test-project"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.8"

organization := "givers.scala"

autoCompilerPlugins := true

addCompilerPlugin("givers.scala.namedargument" %% "scala-named-argument-compiler-plugin" % "0.1.1-SNAPSHOT")

mainClass := Some("givers.scala.namedargument.test.Test")

scalacOptions ++= Seq(
  "-P:named-argument:annotation:givers.scala.namedargument.test.ForceNamedArgument"
)