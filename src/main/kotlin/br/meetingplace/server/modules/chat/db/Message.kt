package br.meetingplace.server.modules.chat.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import java.time.LocalDateTime

object Message: Table("chat_message"){
    var content = varchar("message_content", 1024)
    val imageURL = varchar("image_url", 256).nullable()
    val id = varchar("message_id", 32)
    val creatorID = varchar("creator_id", 32).references(User.id)
    val type = short("message_type")
    val chatID = varchar("chat_id", 32).references(Chat.id)
    val creationDate = date("date_of_creation")

    override val primaryKey = PrimaryKey(id)
}