package br.meetingplace.server.modules.conversation.entities.messages

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageEntity: Table("messages"){
    val content = varchar("message_content", 4096)
    val imageURL = varchar("image_url", 256).nullable()
    val id = varchar("message_id", 36)
    val creatorID = varchar("creator_id", 320).references(User.email, onDelete = ReferenceOption.CASCADE)
    val conversationID = varchar("conversation_id", 36).references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE)
    val type = short("message_type")
    val valid = long("valid_until")
    val creationDate = long("creation_date")
    val seenByEveryone = bool("seen_by_everyone")
    override val primaryKey = PrimaryKey(id)
}