package br.meetingplace.server.modules.conversation.entities.conversation

import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ConversationEntity: Table("conversation"){
    val id= varchar("id", 36)
    val name= varchar("name", 128)
    val about= varchar("about", 512).nullable()
    val imageURL = varchar("image_url", 256000).nullable()
    val creationDate = datetime("created_in")
    val isGroup = bool("is_group")
    val latestMessage = long("latest_message").nullable()
    override val primaryKey = PrimaryKey(id)
}
