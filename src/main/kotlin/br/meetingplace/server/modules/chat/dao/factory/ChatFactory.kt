package br.meetingplace.server.modules.chat.dao.factory

import br.meetingplace.server.modules.chat.db.Chat
import br.meetingplace.server.modules.chat.db.ChatOwner
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.chat.data.ChatCreationData
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

object ChatFactory {
    fun createChat(data: ChatCreationData): Status{
        return try {
            if(!User.select { User.id eq data.userID }.empty() && !User.select { User.id eq data.receiverID }.empty() &&
                    ChatOwner.select { (ChatOwner.userID eq  data.userID) and (ChatOwner.receiverID eq data.receiverID)}.empty() &&
                    ChatOwner.select { (ChatOwner.userID eq  data.receiverID) and (ChatOwner.receiverID eq data.userID)}.empty()){
                val chatID = UUID.randomUUID().toString()
                Chat.insert {
                    it[id] = chatID
                    it[groupID] = null
                    it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                }
                ChatOwner.insert {
                    it[this.chatID] = chatID
                    it[userID] = data.userID
                    it[receiverID] = data.receiverID
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}