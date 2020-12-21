package br.meetingplace.server.modules.conversation.dao.messages.status

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageStatusDTO
import br.meetingplace.server.modules.conversation.entities.MessageStatusEntity
import br.meetingplace.server.modules.conversation.entities.MessageStatusEntity.seen
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageStatusDAO: MSI {
    override fun create(conversationID: String, userID: String, messageID: String): HttpStatusCode {
        return try{
            transaction {
                MessageStatusEntity.insert {
                    it[this.conversationID] = conversationID
                    it[this.userID] = userID
                    it[this.messageID] = messageID
                    it[this.seen] = false
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun seenByEveryoneByMessage(messageID: String, conversationID: String): Boolean {
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
    override fun readUnseen(conversationID: String, userID: String): List<MessageStatusDTO> {
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

    override fun update(conversationID: String, userID: String, messageID: String): HttpStatusCode {
        return try{
            transaction {
                MessageStatusEntity.update({ (MessageStatusEntity.conversationID eq conversationID) and
                        (MessageStatusEntity.userID eq userID) and
                        (MessageStatusEntity.messageID eq messageID)}) {

                    it[seen] = true
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
            seen = it[MessageStatusEntity.seen]
        )
    }
}