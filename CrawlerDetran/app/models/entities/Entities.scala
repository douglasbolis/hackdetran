package models.entities

import models.CrawlerDetranContext._

/**
 * Created by clayton on 25/06/15.
 */

//authorizations:
//
//network     - Varchar(255)  #Twitter/Facebook/Openid/whatever
//network_id  - varchar(255)  #Users id for that social network.
//user_id     - int
//
//users:
//
//id (primary, auto increment)
//name
//password
//username

class User( var first_name: String,
            var last_name: Option[String] ,
            var gender: Option[String] ,
            var link: Option[String] ,
            var email: String,
            var password: Option[String],
            var locale: Option[String],
            var timeZone: Option[String]) extends Entity

class UserAuth(
             var user: User,
             var network: String,
             var network_id: String) extends Entity

class Veiculo(var user: User, var placa: String, var renavam: String) extends Entity

class VeiculoRegistro (
                        var veiculo: Veiculo,
                        var reg02: String ,
                        var reg03: Option[String],
                        var reg04 : Option[String],
                        var reg10: Option[String]
                      ) extends Entity


class VeiculoEvento ( var title: String ,
                      var description: String ,
                      var iddescription : String,
                      var isRead: Boolean) extends Entity
