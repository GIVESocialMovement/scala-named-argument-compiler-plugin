name := "scala-named-argument-compiler-plugin"

version := "0.1.1-SNAPSHOT"

scalaVersion := "2.12.8"

organization := "givers.scala.namedargument"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % "2.12.8" withSources()
)

publishMavenStyle := true

bintrayOrganization := Some("givers")

bintrayRepository := "maven"

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT")))

