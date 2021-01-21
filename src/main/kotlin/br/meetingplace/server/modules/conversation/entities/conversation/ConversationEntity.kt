package br.meetingplace.server.modules.conversation.entities.conversation

import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ConversationEntity: Table("conversation"){
    val id= long("conversation_pk").autoIncrement()
    val name= text("name")
    val about= text("about").nullable()
    val imageURL = text("pic").nullable()
    val creationDate = long("date_of_creation")
    val isGroup = bool("is_group")
    val latestMessage = long("latest_message_send_date").nullable()
    override val primaryKey = PrimaryKey(id)
}
