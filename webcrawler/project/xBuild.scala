import play.PlayImport._
import play.PlayScala
import sbt.Build
import sbt.Keys._
import sbt._

object xBuild extends Build {




  // Resolvers
  lazy val local = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  lazy val typesafe = "Typesafe" at "http://repo.typesafe.com/typesafe/releases"
  lazy val xdevel = "nexus.xdevel.com.br" at "http://nexus.xdevel.com.br/content/repositories/releases"
  lazy val maven2 = "maven2" at "https://repo1.maven.org/maven2"
  lazy val fwbrasil = "fwbrasil.net" at "http://repo1.maven.org/maven2"


  name := "webcrawler"

  organization := "br.com.xdevel"

  version := "1.0"


  def commonSettings =
    Seq(
      scalaVersion := "2.11.6",
      exportJars := true,
      resolvers ++= Seq(
        xdevel,
        local,
        typesafe,
        maven2,
        fwbrasil
      )
    )  ++  net.virtualvoid.sbt.graph.Plugin.graphSettings



  lazy val `webcrawler` = (project in file(".")).enablePlugins(PlayScala)

  scalaVersion := "2.11.6"

  libraryDependencies ++= Seq( jdbc , anorm , cache , ws )

  lazy val integrator = Project(
    id = "integrator",
    base = file("integrator"),
    settings = commonSettings
  )





  unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )






}