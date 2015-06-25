import play.PlayImport._
import play.PlayScala
import sbt.Build
import sbt.Keys._
import sbt._

object xBuild extends Build {


  // packages
  lazy val scalaj =  "org.scalaj" %% "scalaj-http" % "1.1.4"
  lazy val htmlcleaner = "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.12"
  lazy val scalaxml = "org.scala-lang.modules" %% "scala-xml" % "1.0.3"
  lazy val jodaTime = "joda-time" % "joda-time" % "2.8.1"
  lazy val h2Database = "com.h2database" % "h2" % "1.4.186"

  lazy val activateVersion = "1.7"
  // activate framework
  lazy val activate = "net.fwbrasil" % "activate_2.11" % activateVersion
  lazy val activatePlay = "net.fwbrasil" % "activate-play_2.11" % activateVersion
  lazy val activateSlick = "net.fwbrasil" % "activate-slick_2.11" % activateVersion
  lazy val activateSprayJson = "net.fwbrasil" % "activate-spray-json_2.11" % activateVersion
  // json manipulators
  lazy val sprayJson = "io.spray" %%  "spray-json" % "1.3.2"






  // Resolvers
  lazy val local = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  lazy val typesafe = "Typesafe" at "http://repo.typesafe.com/typesafe/releases"
  lazy val xdevel = "nexus.xdevel.com.br" at "http://nexus.xdevel.com.br/content/repositories/releases"
  lazy val maven2 = "maven2" at "https://repo1.maven.org/maven2"
  lazy val fwbrasil = "fwbrasil.net" at "http://repo1.maven.org/maven2"





  def commonSettings =
    Seq(
      scalaVersion := "2.11.6",
      version := "1.0",
      organization := "br.com.xdevel",
      exportJars := true,
      resolvers ++= Seq(
        xdevel,
        local,
        typesafe,
        maven2,
        fwbrasil
      )
    )  ++  net.virtualvoid.sbt.graph.Plugin.graphSettings




  // without new dependencies
  lazy val integrator = Project(
    id = "integrator",
    base = file("integrator"),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= Seq(scalaj,htmlcleaner,scalaxml,jodaTime))
  )




  // crawler detran
  lazy val root = Project(id = "CrawlerDetran",
    base = file("."),
    aggregate = Seq(integrator),
    dependencies = Seq(integrator),
    settings = commonSettings ++ Seq(
    libraryDependencies ++= Seq( jdbc , anorm , cache , ws, activate , activatePlay, activateSlick, activateSprayJson, h2Database ,sprayJson))
  ) enablePlugins PlayScala






  unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

}