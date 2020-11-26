package br.meetingplace.server.modules.chatTODOTRANSACTIONS.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ChatOwner: Table("chat_owner"){
    val chatID = varchar("chat_id", 32).references(Chat.id, onDelete = ReferenceOption.CASCADE)
    val userID = varchar("user_id", 32).references(User.id, onDelete = ReferenceOption.CASCADE)
    val receiverID = varchar("receiver_id", 32).references(User.id, onDelete = ReferenceOption.CASCADE)
}