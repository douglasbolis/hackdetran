package models

import models.entities._
import models.CrawlerContext._
import net.fwbrasil.activate.migration.Migration
/**
 * Created by clayton on 25/06/15.
 */
class CrawlerMigration extends Migration {
  def timestamp = 201208191834l
  def up = {

    createTableForAllEntities
      .ifNotExists


    table[User]
      .createTable(
        _.column[String]("first_name"),
        _.column[String]("last_name"),
        _.column[String]("gender"),
        _.column[String]("link"),
        _.column[String]("email"),
        _.column[String]("locale"),
        _.column[String]("timeZone")

      ).ifNotExists

    table[UserAuth]
      .createTable(
        _.column[String]("network"),
        _.column[String]("network_id")
      ).ifNotExists

    table[UserAuth]
      .addReference("user", table[User], "fkUserUserAuth")
      .ifNotExists

    table[Veiculo]
      .createTable(
        _.column[String]("placa"),
        _.column[String]("renavam")
      ).ifNotExists

    table[Veiculo]
      .addReference("user", table[User], "fkUserVeiculo")
      .ifNotExists

    table[VeiculoRegistro]
      .createTable(
        _.column[String]("reg02"),
        _.column[String]("reg03"),
        _.column[String]("reg04"),
        _.column[String]("reg10")
      ).ifNotExists

    table[VeiculoRegistro]
      .addReference("veiculo", table[Veiculo], "fkVeiculoVeiculoRegistro")
      .ifNotExists

    table[VeiculoEvento]
      .createTable(
        _.column[String]("title"),
        _.column[String]("description"),
        _.column[String]("iddescription"),
        _.column[Boolean]("isRead")
      ).ifNotExists

    table[VeiculoEvento]
      .addReference("veiculo", table[Veiculo], "fkVeiculoVeiculoEvento")
      .ifNotExists

  }
}





