package br.meetingplace.server.modules.conversation.dao.messages

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageDTO
import br.meetingplace.server.modules.conversation.entities.MessageEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageDAO: MI{
    override fun create(message: String, imageURL: String?, conversationID: String, creator: String, messageID: String): HttpStatusCode {
        return try {
            transaction {
                MessageEntity.insert {
                    it[this.id] = messageID
                    it[valid] = 0
                    it[content] = message
                    it[this.creatorID] = creator
                    it[this.imageURL] = imageURL
                    it[type] = 0
                    it[this.conversationID] = conversationID
                    it[creationDate] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun update(messageID: String): HttpStatusCode {
        return try {
            transaction {
                MessageEntity.update( {
                    MessageEntity.id eq messageID
                }){
                    it[valid] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override fun delete(messageID: String): HttpStatusCode {
        return try {
            transaction {
                MessageEntity.deleteWhere {
                    MessageEntity.id eq messageID
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override fun check(messageID: String): Boolean {
        return try {
            !transaction {
                MessageEntity.select {
                    MessageEntity.id eq messageID
                }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
    override fun read(messageID: String): MessageDTO? {
        return try {
            transaction {
                MessageEntity.deleteWhere {
                    ((MessageEntity.valid.less(System.currentTimeMillis().toInt())) and (MessageEntity.valid neq 0))   or (MessageEntity.valid eq System.currentTimeMillis())
                }
            }
            transaction {
                MessageEntity.select {
                    MessageEntity.id eq messageID
                }.map { mapMessage(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override fun readAllConversation(userID: String, conversationID: String): List<MessageDTO> {
        return try {
            val conversation  = mutableListOf<MessageDTO>()
//            transaction {
//                Message.update({(Message.valid eq 0) and (Message.read eq false) and (Message.creatorID neq userID)}){
//                    it[valid] = System.currentTimeMillis() + 86400000 //24hours + current time
//                    it[read] = true
//
//                }
//            }
            transaction {
                MessageEntity.deleteWhere {
                    ((MessageEntity.valid.less(System.currentTimeMillis().toInt())) and (MessageEntity.valid neq 0))   or (MessageEntity.valid eq System.currentTimeMillis())
                }
            }
            conversation.addAll(transaction {
                MessageEntity.select {
                    MessageEntity.conversationID eq conversationID
                }.map { mapMessage(it) }
            })
            return conversation
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    private fun mapMessage(it: ResultRow): MessageDTO {
        return MessageDTO(
            content = it[MessageEntity.content],
            imageURL = it[MessageEntity.imageURL],
            id = it[MessageEntity.id],
            valid = it[MessageEntity.valid],
            creatorID = it[MessageEntity.creatorID],
            type =  it[MessageEntity.type],
            conversationID = it[MessageEntity.conversationID],
            creationDate = it[MessageEntity.creationDate])
    }
}