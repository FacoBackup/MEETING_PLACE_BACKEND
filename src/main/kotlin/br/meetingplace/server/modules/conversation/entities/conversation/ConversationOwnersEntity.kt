package br.meetingplace.server.modules.conversation.entities.conversation

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ConversationOwnersEntity: Table("conversation_owners") {
    val primaryUserID = long("primary_user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val conversationID = long("conversation_id").references(ConversationEntity.id, onDelete = ReferenceOption.CASCADE)
    val secondaryUserID = long("secondary_user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
}