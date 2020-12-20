package br.meetingplace.server.modules.conversation.entities

import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageOpinionEntity: Table("message_opinions"){
    val liked= bool("liked")
    val messageID = varchar("message_id", 36).references(MessageEntity.id, onDelete = ReferenceOption.CASCADE)
    val userID = varchar("user_id", 320).references(User.email, onDelete = ReferenceOption.SET_NULL)
}
