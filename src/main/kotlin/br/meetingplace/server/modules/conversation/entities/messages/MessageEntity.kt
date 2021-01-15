package br.meetingplace.server.modules.conversation.entities.messages

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageEntity: Table("messages"){
    val content = text("message_content")
    val imageURL = text("image_url").nullable()
    val id = varchar("message_id", 36)
    val creatorID = varchar("creator_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)
    val conversationID = varchar("conversation_id", 36).references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE)
    val type = short("message_type")
    val valid = long("valid_until")
    val creationDate = long("creation_date")
    val page = long("page")
    val seenByEveryone = bool("seen_by_everyone")
    override val primaryKey = PrimaryKey(id)
}