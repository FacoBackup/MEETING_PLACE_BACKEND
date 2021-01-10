package br.meetingplace.server.modules.conversation.entities.conversation

import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ConversationEntity: Table("conversation"){
    val id= varchar("id", 36)
    val name= text("name")
    val about= text("about").nullable()
    val imageURL = text("image_url").nullable()
    val creationDate = long("created_in")
    val isGroup = bool("is_group")
    val latestMessage = long("latest_message").nullable()
    override val primaryKey = PrimaryKey(id)
}
