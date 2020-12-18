package br.meetingplace.server.modules.conversation.dao.owners

import br.meetingplace.server.modules.conversation.dto.response.ConversationOwnersDTO
import br.meetingplace.server.modules.conversation.entities.ConversationOwners
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object ConversationOwnersDAO: COI {
    override fun create(userID: String, secondUserID: String, conversationID: String): HttpStatusCode {
        return try{
            transaction{
                ConversationOwners.insert {
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

    override fun readAll(userID: String): List<ConversationOwnersDTO> {
        return try{
            transaction{
                ConversationOwners.select {
                    (ConversationOwners.primaryUserID eq userID) or
                    (ConversationOwners.secondaryUserID eq userID)
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
                ConversationOwners.select {
                    (((ConversationOwners.primaryUserID eq userID) and
                    (ConversationOwners.secondaryUserID eq secondUserID)) or
                    ((ConversationOwners.primaryUserID eq secondUserID) and
                    (ConversationOwners.secondaryUserID eq userID)))
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
            primaryUserID = it[ConversationOwners.primaryUserID],
            secondaryUserID = it[ConversationOwners.secondaryUserID],
            conversationID = it[ConversationOwners.conversationID]
        )
    }
}