package br.meetingplace.server.modules.conversation.entities

import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ConversationOwners: Table("conversation_owners") {
    val primaryUserID = varchar("primary_id", 320).references(User.email, onDelete = ReferenceOption.CASCADE)
    val conversationID = varchar("conversation_id", 320).references(Conversation.id, onDelete = ReferenceOption.CASCADE)
    val secondaryUserID = varchar("secondary_id", 320).references(User.email, onDelete = null)
}