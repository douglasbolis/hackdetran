import play.PlayScala
import play.PlayImport._
import sbt.Build
import sbt.Keys._
import sbt._

object xBuild extends Build {


  //versions
  lazy val activateVersion = "1.7"
  lazy val scalaTestVersion = "2.2.4"
  lazy val scalajVersion = "1.1.4"
  lazy val htmlcleanerVersion = "2.12"
  lazy val scalaxmlVersion = "1.0.3"
  lazy val jodaTimeVersion = "2.8.1"
  lazy val h2DatabaseVersion = "1.4.186"
  lazy val sprayJsonVersion = "1.3.2"




  // packages
  lazy val scalaj =  "org.scalaj" %% "scalaj-http" % scalajVersion
  lazy val htmlcleaner = "net.sourceforge.htmlcleaner" % "htmlcleaner" % htmlcleanerVersion
  lazy val scalaxml = "org.scala-lang.modules" %% "scala-xml" % scalaxmlVersion
  lazy val jodaTime = "joda-time" % "joda-time" % jodaTimeVersion
  lazy val h2Database = "com.h2database" % "h2" % h2DatabaseVersion

  // activate framework
  lazy val activate = "net.fwbrasil" % "activate_2.11" % activateVersion
  lazy val activatePlay = "net.fwbrasil" % "activate-play_2.11" % activateVersion
  lazy val activateSlick = "net.fwbrasil" % "activate-slick_2.11" % activateVersion
  lazy val activateSprayJson = "net.fwbrasil" % "activate-spray-json_2.11" % activateVersion
  // json manipulators
  lazy val sprayJson = "io.spray" %%  "spray-json" % sprayJsonVersion

  //testers
  lazy val scalaTest = "org.scalatest" % "scalatest_2.11" % scalaTestVersion % "test"
  lazy val activateTest = "net.fwbrasil" % "activate-test_2.11" % activateVersion % "test"







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
      libraryDependencies ++= Seq(scalaj, htmlcleaner,
                                  scalaxml,jodaTime))
  )



//  lazy val models = Project(
//    id = "models",
//    base = file("models"),
//    aggregate = Seq(integrator),
//    dependencies = Seq(integrator),
//    settings = commonSettings ++ Seq(
//      libraryDependencies ++= Seq(
//        activate, activateSlick, activateSprayJson , activatePlay, h2Database ,sprayJson))
//  )




  // crawler detran
  lazy val root = Project(id = "CrawlerDetran",
    base = file("."),
    aggregate = Seq(integrator),
    dependencies = Seq(integrator),
    settings = commonSettings ++ Seq(
    libraryDependencies ++= Seq( cache , activate, activateSlick , activatePlay,
       filters ,  jdbc , h2Database ))
  )  enablePlugins PlayScala






  unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

}


