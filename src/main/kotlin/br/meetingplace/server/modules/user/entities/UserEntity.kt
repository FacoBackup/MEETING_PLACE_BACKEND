package br.meetingplace.server.modules.user.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date

object UserEntity: Table("users"){
    val email = varchar("user_id", 320)
    val userName = varchar("user_name", 64)
    val birth = long("born_date")
    val password = varchar("password", 512)
    val gender = varchar("user_gender", 32)
    val nationality = varchar("user_nationality", 256).nullable()
    val about = text("user_about").nullable()
    val imageURL = text("user_image_url").nullable()
    val backgroundImageURL = text("user_background_image_url").nullable()
    val phoneNumber = varchar("user_number", 32).nullable()
    val cityOfBirth = varchar("city_of_birth", 256).nullable()
    val admin = bool("is_admin")
    val joinedIn = long("joined_in")
    override val primaryKey = PrimaryKey(email)
}