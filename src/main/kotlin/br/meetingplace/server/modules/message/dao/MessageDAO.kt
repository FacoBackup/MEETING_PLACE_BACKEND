package br.meetingplace.server.modules.message.dao

import br.meetingplace.server.modules.message.entities.Message
import br.meetingplace.server.modules.message.dto.response.MessageDTO
import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object MessageDAO: MI{
    override fun create(data: RequestMessageCreation): Status {
        return try {
            transaction {
                Message.insert {
                    it[id] = UUID.randomUUID().toString()
                    it[creationDate] = DateTime.now()
                    it[content] = data.message
                    it[creatorID] = data.userID
                    it[imageURL] = data.imageURL
                    it[type] = 0
                    when(data.isGroup){
                        true-> it[groupReceiverID] = data.receiverID
                        false -> it[userReceiverID] = data.receiverID
                    }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(messageID: String): Status {
        return try {
            transaction {
                Message.deleteWhere {
                    Message.id eq messageID
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun readAllFromCreator(creatorID: String, receiverID: String, isGroup: Boolean): List<MessageDTO> {
        return try {
            transaction {
                Message.select {
                    (Message.creatorID eq creatorID) and
                    when(isGroup){
                        true->(Message.groupReceiverID eq receiverID)
                        false->(Message.userReceiverID eq receiverID)
                    }
                }.map { mapMessage(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
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
    override fun readAllConversation(userID: String, receiverID: String, isGroup: Boolean): List<MessageDTO> {
        return try {
            transaction {
                Message.select {
                    when(isGroup){
                        true->(Message.groupReceiverID eq receiverID)
                        false->{
                            ((Message.userReceiverID eq receiverID) and
                            (Message.creatorID eq userID)) or
                            ((Message.userReceiverID eq userID) and
                            (Message.creatorID eq receiverID))
                        }
                    }
                }.map { mapMessage(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    private fun mapMessage(it: ResultRow): MessageDTO {
        return MessageDTO(content = it[Message.content], imageURL = it[Message.imageURL],
                id = it[Message.id], creationDate = it[Message.creationDate].toString("dd-MM-yyyy"),
                creatorID = it[Message.creatorID], type =  it[Message.type],receiverID = it[Message.userReceiverID], groupID = it[Message.groupReceiverID])
    }
}