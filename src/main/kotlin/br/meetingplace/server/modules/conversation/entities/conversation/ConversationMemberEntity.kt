package br.meetingplace.server.modules.conversation.entities.conversation

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ConversationMemberEntity: Table("conversation_members") {
    val conversationID = varchar("conversation_id", 36).references(ConversationEntity.id,onDelete = ReferenceOption.CASCADE)
    var admin = bool("is_admin")
    val userID = varchar("user_id", 36).references(UserEntity.email,onDelete = ReferenceOption.CASCADE)
}