package br.meetingplace.server.modules.conversation.dao.conversation.owners

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationOwnersDTO
import br.meetingplace.server.modules.conversation.entities.ConversationOwnersEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object ConversationOwnersDAO: COI {
    override fun create(userID: String, secondUserID: String, conversationID: String): HttpStatusCode {
        return try{
            transaction{
                ConversationOwnersEntity.insert {
                    it[this.primaryUserID] = userID
                    it[this.secondaryUserID] = secondUserID
                    it[this.conversationID] = conversationID
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun check(userID: String, secondUserID: String): Boolean {
        return try{
            transaction{
                    ConversationOwnersEntity.select {
                        (((ConversationOwnersEntity.primaryUserID eq userID) and
                                (ConversationOwnersEntity.secondaryUserID eq secondUserID)) or
                                ((ConversationOwnersEntity.primaryUserID eq secondUserID) and
                                        (ConversationOwnersEntity.secondaryUserID eq userID)))
                    }.map{ mapConversationOwners(it = it) }.firstOrNull()
                } != null

        }catch (e: Exception) {
            false
        }catch (psql: PSQLException){
            false
        }
    }
    override fun readAll(userID: String): List<ConversationOwnersDTO> {
        return try{
            transaction{
                ConversationOwnersEntity.select {
                    (ConversationOwnersEntity.primaryUserID eq userID) or
                    (ConversationOwnersEntity.secondaryUserID eq userID)
                }.map{ mapConversationOwners(it = it) }
            }
        }catch (e: Exception) {
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override fun read(userID: String, secondUserID: String): ConversationOwnersDTO? {
        return try{
            transaction{
                ConversationOwnersEntity.select {
                    (((ConversationOwnersEntity.primaryUserID eq userID) and
                    (ConversationOwnersEntity.secondaryUserID eq secondUserID)) or
                    ((ConversationOwnersEntity.primaryUserID eq secondUserID) and
                    (ConversationOwnersEntity.secondaryUserID eq userID)))
                }.map{ mapConversationOwners(it = it) }.firstOrNull()
            }
        }catch (e: Exception) {
            null
        }catch (psql: PSQLException){
            null
        }
    }
    private fun mapConversationOwners(it: ResultRow): ConversationOwnersDTO {
        return ConversationOwnersDTO(
            primaryUserID = it[ConversationOwnersEntity.primaryUserID],
            secondaryUserID = it[ConversationOwnersEntity.secondaryUserID],
            conversationID = it[ConversationOwnersEntity.conversationID]
        )
    }
}