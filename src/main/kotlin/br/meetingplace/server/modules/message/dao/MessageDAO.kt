package br.meetingplace.server.modules.message.dao

import br.meetingplace.server.modules.message.dto.response.MessageDTO
import br.meetingplace.server.modules.message.entities.Message
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException
import java.util.*

object MessageDAO: MI{
    override fun create(message: String, imageURL: String?, to: String, from: String, isGroup: Boolean): HttpStatusCode {
        return try {
            transaction {
                Message.insert {
                    it[id] = UUID.randomUUID().toString()
                    it[valid] = 0
                    it[content] = message
                    it[creatorID] = from
                    it[this.imageURL] = imageURL
                    it[type] = 0
                    it[read] = false
                    when(isGroup){
                        true-> it[groupReceiverID] = to
                        false -> it[userReceiverID] = to
                    }
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun delete(messageID: String): HttpStatusCode {
        return try {
            transaction {
                Message.deleteWhere {
                    Message.id eq messageID
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
                Message.select {
                    Message.id eq messageID
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
                Message.select {
                    Message.id eq messageID
                }.map { mapMessage(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override fun readAllConversation(userID: String, subjectID: String, isGroup: Boolean): List<MessageDTO> {
        return try {
            val conversation  = mutableListOf<MessageDTO>()
            transaction {
                Message.update({(Message.valid eq 0) and (Message.read eq false) and (Message.creatorID eq subjectID)}){
                    it[valid] = System.currentTimeMillis().toInt() + 86400000 //24hours + current time
                    it[read] = true
                }
            }
            transaction {
                Message.deleteWhere {
                    ((Message.valid.less(System.currentTimeMillis().toInt())) and (Message.valid neq 0))   or (Message.valid eq System.currentTimeMillis().toInt())
                }
            }
            conversation.addAll(transaction {
                Message.select {
                    when(isGroup){
                        true->(Message.groupReceiverID eq subjectID)
                        false->{
                            (((Message.userReceiverID eq subjectID) and
                            (Message.creatorID eq userID)) or
                            ((Message.userReceiverID eq userID) and
                            (Message.creatorID eq subjectID)))
                        }
                    }
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
            content = it[Message.content],
            imageURL = it[Message.imageURL],
            id = it[Message.id],
            valid = it[Message.valid],
            creatorID = it[Message.creatorID],
            type =  it[Message.type],
            receiverID = it[Message.userReceiverID],
            groupID = it[Message.groupReceiverID],
            read = it[Message.read])
    }
}