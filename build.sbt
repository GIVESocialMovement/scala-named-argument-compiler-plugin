name := "scala-named-argument-compiler-plugin"

version := "0.1.2"

scalaVersion := "2.13.8"

organization := "io.github.givesocialmovement"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % "2.13.8" withSources()
)

publishMavenStyle := true

bintrayOrganization := Some("givers")

bintrayRepository := "maven"

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"

Test / publishArtifact := false

pomIncludeRepository := { _ => false }

licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT")))

publishMavenStyle := true

publishTo := sonatypePublishToBundle.value