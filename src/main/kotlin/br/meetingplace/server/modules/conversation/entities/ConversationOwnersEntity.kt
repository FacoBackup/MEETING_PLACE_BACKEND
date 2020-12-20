package br.meetingplace.server.modules.conversation.entities

import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ConversationOwnersEntity: Table("conversation_owners") {
    val primaryUserID = varchar("primary_id", 320).references(User.email, onDelete = ReferenceOption.CASCADE)
    val conversationID = varchar("conversation_id", 36).references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE)
    val secondaryUserID = varchar("secondary_id", 320).references(User.email, onDelete = null)
}