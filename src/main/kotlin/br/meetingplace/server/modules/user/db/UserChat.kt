package br.meetingplace.server.modules.chat.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date

object UserChat: Table("chat") {
    val id = varchar("chat_id", 32)
    val creationDate = date("date_of_creation")
    override val primaryKey = PrimaryKey(id)
}