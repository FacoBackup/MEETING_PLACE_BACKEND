package br.meetingplace.server.modules.user.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime

object User: Table("users"){
    val email = varchar("user_id", 128)
    val userName = varchar("user_name", 64)
    val birth = date("date_of_birth")
    val password = varchar("password", 256)
    val gender = varchar("user_gender", 64)
    val nationality = varchar("user_nationality", 64)
    val about = varchar("user_about", 64).nullable()
    val imageURL = varchar("user_image_url", 256).nullable()
    val phoneNumber = varchar("user_number", 32).nullable()
    val cityOfBirth = varchar("city_of_birth", 32)
    val admin = bool("is_admin")
    override val primaryKey = PrimaryKey(email)
}