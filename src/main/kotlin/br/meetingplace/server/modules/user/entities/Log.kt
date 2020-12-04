package br.meetingplace.server.modules.user.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Log: Table("logs") {
    val userID = varchar("user_id", 36).references(User.email, onDelete = ReferenceOption.CASCADE)
    val ip = varchar("ip_address", 64)
    val timeOfLogin = datetime("date_of_login")
}