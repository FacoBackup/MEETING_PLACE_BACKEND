package br.meetingplace.server.modules.user.entitie

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object User: Table("user"){
    val id = varchar("user_id", 36)
    var userName = varchar("user_name", 64)
    var birth = datetime("date_of_birth")
    var email = varchar("user_email", 64)
    var gender = varchar("user_gender", 64)
    var nationality = varchar("user_nationality", 64)
    var about = varchar("user_about", 64).nullable()
    var imageURL = varchar("user_image_url", 256).nullable()
    val phoneNumber = varchar("user_number", 32).nullable()
    val cityOfBirth = varchar("city_of_birth", 32)

    override val primaryKey = PrimaryKey(id)
}