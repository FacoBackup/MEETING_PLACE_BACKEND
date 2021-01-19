package br.meetingplace.server.modules.conversation.entities.notification

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageNotificationEntity: Table("message_notification") {
    val messageID = varchar("message_id", 36).references(MessageEntity.id, onDelete = null)
    val conversationID = varchar("conversation_id", 36).references(ConversationEntity.id,onDelete = ReferenceOption.CASCADE)
    val userID = varchar("user_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)

    val isGroup = bool("is_group")
    val seenAt = long("seen_at").nullable()
    val creationDate = long("creation_date")

    val page = long("page")
}