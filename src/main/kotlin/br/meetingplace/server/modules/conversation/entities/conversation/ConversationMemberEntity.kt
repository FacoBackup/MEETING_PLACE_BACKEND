package br.meetingplace.server.modules.conversation.entities.conversation

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ConversationMemberEntity: Table("conversation_members") {
    val conversationID = long("conversation_pk").references(ConversationEntity.id,onDelete = ReferenceOption.CASCADE)
    var admin = bool("is_admin")
    val userID = long("user_pk").references(UserEntity.id,onDelete = ReferenceOption.CASCADE)
}