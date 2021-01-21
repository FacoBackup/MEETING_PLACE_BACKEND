package br.meetingplace.server.modules.user.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date

object UserEntity: Table("users"){

    val id = long("user_pk").autoIncrement()

    val email = varchar("email", 320)
    val category = text("category_work").nullable()
    val name = text("name")
    val userName = varchar("user_name",128)
    val birth = long("birth")
    val password = varchar("password", 512)
    val gender = varchar("gender", 32)
    val nationality = varchar("nationality", 256).nullable()
    val about = text("about").nullable()
    val pic = text("pic").nullable()
    val background = text("background").nullable()
    val phoneNumber = varchar("phone", 32).nullable()
    val cityOfBirth = varchar("city_of_birth", 256).nullable()
    val joinedIn = long("joined_in")

    override val primaryKey = PrimaryKey(id)
}