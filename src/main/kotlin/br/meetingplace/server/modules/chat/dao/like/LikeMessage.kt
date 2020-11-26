package br.meetingplace.server.modules.chat.dao.like

import br.meetingplace.server.modules.chat.db.Message
import br.meetingplace.server.modules.chat.db.MessageOpinions
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object LikeMessage{

    fun favoriteMessage(data: ChatSimpleOperator): Status {
        return try {
            if(!transaction { Message.select { (Message.chatID eq data.chatID) and (Message.id eq data.messageID)} }.empty() &&
                    !transaction { User.select { User.id eq data.userID } }.empty()){
                val opinion = transaction { MessageOpinions.select { (MessageOpinions.chatID eq data.chatID) and (MessageOpinions.messageID eq data.messageID) and (MessageOpinions.userID eq data.userID) } }.firstOrNull()
                when(opinion == null){
                    true->{ //nothing yet
                        transaction {
                            MessageOpinions.insert {
                                it[chatID] = data.chatID
                                it[userID] = data.userID
                                it[messageID] = data.messageID
                                it[liked] = true
                            }
                        }
                    }
                    false->{
                        transaction {
                            MessageOpinions.update ({ (MessageOpinions.chatID eq data.chatID) and (MessageOpinions.messageID eq data.messageID) and (MessageOpinions.userID eq data.userID)}){
                                it[liked] = true
                            }
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