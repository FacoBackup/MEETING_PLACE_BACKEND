package br.meetingplace.server.modules.conversation.dao.messages.status

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageStatusDTO
import br.meetingplace.server.modules.conversation.entities.messages.MessageStatusEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageStatusDAO: MSI {
    override suspend fun create(conversationID: Long, userID: Long, messageID: Long): HttpStatusCode {
        return try{
            transaction {
                MessageStatusEntity.insert {
                    it[this.conversationID] = conversationID
                    it[this.userID] = userID
                    it[this.messageID] = messageID
                    it[this.seen] = false
                    it[this.seenAt] = null
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun seenByEveryoneByMessage(messageID: Long, conversationID: Long): Boolean {
        return try {
            transaction {
                MessageStatusEntity.select {
                    (MessageStatusEntity.conversationID eq conversationID) and
                    (MessageStatusEntity.seen eq false) and (MessageStatusEntity.messageID eq messageID)
                }.map{ mapMessageStatus(it) }.firstOrNull()
            } == null

        } catch (e: Exception) {
            false
        } catch (psql: PSQLException) {
            false
        }
    }
    override suspend fun readAllUnseenMessages(conversationID: Long, userID: Long): List<MessageStatusDTO> {
        return try {
            transaction {
                MessageStatusEntity.select {
                    (MessageStatusEntity.conversationID eq conversationID) and
                    (MessageStatusEntity.userID eq userID) and (MessageStatusEntity.seen eq false)
                }.map{ mapMessageStatus(it) }
            }
        } catch (e: Exception) {
            listOf()
        } catch (psql: PSQLException) {
            listOf()
        }
    }

    override suspend fun unseenMessagesCount(conversationID: Long, userID: Long): Long {
        return try{
            transaction {
                MessageStatusEntity.select{
                    (MessageStatusEntity.conversationID eq conversationID) and
                    (MessageStatusEntity.userID eq userID) and
                    (MessageStatusEntity.seen eq false)
                }.count()
            }
        }catch (e: Exception){
            0
        }catch (psql: PSQLException){
            0
        }
    }
    override suspend fun update(conversationID: Long, userID: Long, messageID: Long): HttpStatusCode {
        return try{
            transaction {
                MessageStatusEntity.update({ (MessageStatusEntity.conversationID eq conversationID) and
                        (MessageStatusEntity.userID eq userID) and
                        (MessageStatusEntity.messageID eq messageID)}) {

                    it[seen] = true
                    it[this.seenAt] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.OK
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapMessageStatus(it: ResultRow): MessageStatusDTO{
        return MessageStatusDTO(
            conversationID = it[MessageStatusEntity.conversationID],
            userID = it[MessageStatusEntity.userID],
            messageID = it[MessageStatusEntity.messageID],
            seen = it[MessageStatusEntity.seen],
            seenAt = it[MessageStatusEntity.seenAt]
        )
    }
}