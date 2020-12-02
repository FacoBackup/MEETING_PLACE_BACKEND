package br.meetingplace.server.modules.message.service.opinion

import br.meetingplace.server.modules.message.entitie.Message
import br.meetingplace.server.modules.message.entitie.MessageOpinion
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.message.MessageDTO
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object MessageOpinion {
    fun dislikeMessage(data: MessageDTO): Status {
        return try {
            if(transaction { !Message.select { Message.id eq data.messageID }.empty()  }&&
               transaction { !User.select { User.id eq data.userID }.empty() }){

                when(transaction { MessageOpinion.select { MessageOpinion.messageID eq data.messageID }.empty() }){
                    true-> transaction {
                        MessageOpinion.insert {
                            it[userID] = data.userID
                            it[messageID] = data.messageID
                            it[liked] = false
                        }
                    }

                    false-> transaction {
                            MessageOpinion.update ({ MessageOpinion.messageID eq data.messageID }){
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

    fun likeMessage(data: MessageDTO): Status {
        return try {
            if(transaction { !Message.select { Message.id eq data.messageID }.empty()  }&&
               transaction { !User.select { User.id eq data.userID }.empty() }){

                when(transaction { MessageOpinion.select { MessageOpinion.messageID eq data.messageID }.empty() }){
                    true-> transaction {
                            MessageOpinion.insert {
                                it[userID] = data.userID
                                it[messageID] = data.messageID
                                it[liked] = true
                            }
                        }

                    false-> transaction {
                            MessageOpinion.update ({ MessageOpinion.messageID eq data.messageID }){
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