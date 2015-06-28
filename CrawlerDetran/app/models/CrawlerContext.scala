package models

import net.fwbrasil.activate.play.ActivatePlayContext

/**
 * Created by clayton on 25/06/15.
 */
object CrawlerContext extends ActivatePlayContext {

  //override  val storage = StorageFactory.fromSystemProperties(this.contextName)


  override protected def entitiesPackages = List("models.entities")

}

