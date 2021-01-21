package br.meetingplace.server.modules.conversation.entities.messages

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageStatusEntity: Table("message_status") {
    val userID = long("user_pk" ).references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val conversationID = long("conversation_pk" ).references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE)
    val messageID = long("message_pk" ).references(MessageEntity.id, onDelete = ReferenceOption.CASCADE)
    val seen = bool("seen")
    val seenAt = long("seen_at").nullable()
}