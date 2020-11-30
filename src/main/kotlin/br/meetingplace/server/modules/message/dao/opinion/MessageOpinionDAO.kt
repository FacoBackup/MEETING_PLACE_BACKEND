package br.meetingplace.server.modules.message.dao.opinion

import br.meetingplace.server.modules.message.db.Message
import br.meetingplace.server.modules.message.db.MessageOpinions
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.message.RequestMessageSimple
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object MessageOpinionDAO {
    fun dislikeMessage(data: RequestMessageSimple): Status {
        return try {
            if(transaction { !Message.select { Message.id eq data.messageID }.empty()  }&&
               transaction { !User.select { User.id eq data.userID }.empty() }){

                when(transaction { MessageOpinions.select { MessageOpinions.messageID eq data.messageID }.empty() }){
                    true-> transaction {
                        MessageOpinions.insert {
                            it[userID] = data.userID
                            it[messageID] = data.messageID
                            it[liked] = false
                        }
                    }

                    false-> transaction {
                            MessageOpinions.update ({ MessageOpinions.messageID eq data.messageID }){
                                it[liked] = false
                            }
                        }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun likeMessage(data: RequestMessageSimple): Status {
        return try {
            if(transaction { !Message.select { Message.id eq data.messageID }.empty()  }&&
               transaction { !User.select { User.id eq data.userID }.empty() }){

                when(transaction { MessageOpinions.select { MessageOpinions.messageID eq data.messageID }.empty() }){
                    true-> transaction {
                            MessageOpinions.insert {
                                it[userID] = data.userID
                                it[messageID] = data.messageID
                                it[liked] = true
                            }
                        }

                    false-> transaction {
                            MessageOpinions.update ({ MessageOpinions.messageID eq data.messageID }){
                                it[liked] = true
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