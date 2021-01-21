package br.meetingplace.server.modules.conversation.entities.messages

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageOpinionEntity: Table("message_opinions"){
    val liked= bool("is_liked")
    val messageID = long("message_pk").references(MessageEntity.id, onDelete = ReferenceOption.CASCADE)
    val userID = long("user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
}
