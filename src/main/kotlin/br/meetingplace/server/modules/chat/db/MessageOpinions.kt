package br.meetingplace.server.modules.chat.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.Table

object MessageOpinions: Table("chat_message_opinion"){
    var liked= bool("liked")
    val chatID = varchar("chat_id", 32).references(Chat.id)
    val messageID = varchar("message_id", 32).references(Message.id)
    val userID = varchar("user_id", 32).references(User.id)
}
