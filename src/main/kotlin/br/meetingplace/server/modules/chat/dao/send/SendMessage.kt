package br.meetingplace.server.modules.chat.dao.send

import br.meetingplace.server.db.mapper.chat.ChatMapperInterface
import br.meetingplace.server.modules.chat.db.Chat
import br.meetingplace.server.modules.chat.db.ChatOwner
import br.meetingplace.server.modules.chat.db.Message
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.chat.data.MessageData
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*


object SendMessage {

    fun sendMessage(data: MessageData, chatMapper: ChatMapperInterface): Status{
        return try {
            val chat = Chat.select {Chat.id eq data.chatID}.firstOrNull()
            if(chat != null)
                when(!chatMapper.mapChat(chat).groupID.isNullOrBlank()){
                    true->{ // user <-> user
                        if(!User.select { User.id eq data.userID }.empty() &&
                           !ChatOwner.select {(ChatOwner.userID eq data.userID) and (ChatOwner.chatID eq data.chatID)}.empty()){
                           Message.insert {
                                it[content] = data.message
                                it[imageURL] = data.imageURL
                                it[id] = UUID.randomUUID().toString()
                                it[chatID] = data.chatID
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                                it[type] = 0
                                it[creatorID] = data.userID
                           }
                        }
                    }
                    false->{ // group
                        if(!User.select { User.id eq data.userID }.empty()){
                            Message.insert {
                                it[content] = data.message
                                it[imageURL] = data.imageURL
                                it[id] = UUID.randomUUID().toString()
                                it[chatID] = data.chatID
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                                it[type] = 0
                                it[creatorID] = data.userID
                            }
                        }
                    }
                }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}