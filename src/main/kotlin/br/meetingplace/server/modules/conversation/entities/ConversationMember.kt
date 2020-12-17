package br.meetingplace.server.modules.conversation.entities

import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ConversationMember: Table("conversation_members") {
    val conversationID = varchar("conversation_id", 256).references(Conversation.id,onDelete = ReferenceOption.CASCADE)
    var admin = bool("is_admin")
    val userID = varchar("user_id", 36).references(User.email,onDelete = ReferenceOption.CASCADE)
}