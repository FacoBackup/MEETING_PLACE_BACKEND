package br.meetingplace.server.modules.conversation.entities.notification

import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageNotificationEntity: Table("message_notification") {
    private val notificationID = long("notification_pk").autoIncrement()

    val messageID = long("message_pk").references(MessageEntity.id, onDelete = ReferenceOption.CASCADE)
    val conversationID = long("conversation_pk").references(ConversationEntity.id,onDelete = ReferenceOption.CASCADE)
    val userID = long("user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)

    val isGroup = bool("is_group")
    val seenAt = long("seen_at").nullable()
    val creationDate = long("date_of_creation")

    val page = long("page")

    override val primaryKey = PrimaryKey(notificationID)
}