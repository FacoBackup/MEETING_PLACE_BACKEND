package br.meetingplace.server.modules.message.service.opinion

import br.meetingplace.server.modules.message.entitie.Message
import br.meetingplace.server.modules.message.entitie.MessageOpinions
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.message.MessageDTO
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object MessageOpinionDAO {
    fun dislikeMessage(data: MessageDTO): Status {
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

    fun likeMessage(data: MessageDTO): Status {
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