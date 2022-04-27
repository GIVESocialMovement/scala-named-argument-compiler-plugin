import sbt.url

name := "scala-named-argument-compiler-plugin"

version := "0.1.2"

scalaVersion := "2.13.8"

organization := "io.github.givesocialmovement"

homepage := Some(url("https://github.com/GIVESocialMovement/scala-named-argument-compiler-plugin"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/GIVESocialMovement/scala-named-argument-compiler-plugin"),
    "scm:git@github.com:GIVESocialMovement/scala-named-argument-compiler-plugin.git"
  )
)

developers := List(
  Developer(id="tanin", name="tanin", email="developers@giveasia.org", url=url("https://github.com/tanin47"))
)

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

publishTo := sonatypePublishToBundle.value