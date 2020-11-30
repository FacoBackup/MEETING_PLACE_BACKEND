package br.meetingplace.server.modules.message.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object MessageOpinions: Table("message_opinion"){
    var liked= bool("liked")
    val messageID = varchar("message_id", 36).references(Message.id, onDelete = ReferenceOption.CASCADE)
    val userID = varchar("user_id", 36).references(User.id, onDelete = ReferenceOption.SET_NULL)
}
