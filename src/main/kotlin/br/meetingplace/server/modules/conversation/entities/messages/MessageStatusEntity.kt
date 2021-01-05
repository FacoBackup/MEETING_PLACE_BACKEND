package br.meetingplace.server.modules.conversation.entities.messages

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageStatusEntity: Table("message_status") {
    val userID = varchar("user_id" ,320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)
    val conversationID = varchar("conversation_id" ,320).references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE)
    val messageID = varchar("message_id" ,320).references(MessageEntity.id, onDelete = ReferenceOption.CASCADE)
    val seen = bool("seen")
}