package br.meetingplace.server.modules.conversation.dao.conversation.member

import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationMemberDTO
import br.meetingplace.server.modules.conversation.entities.conversation.ConversationMemberEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object ConversationMemberDAO: CMI {

    override suspend fun create(userID: Long, conversationID: Long, admin: Boolean): HttpStatusCode {
        return try {
            transaction {
                ConversationMemberEntity.insert {
                    it[this.conversationID] = conversationID
                    it[this.userID] = userID
                    it[this.admin] = admin
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun delete(userID: Long, conversationID: Long): HttpStatusCode {
        return try {
            transaction {
                ConversationMemberEntity.deleteWhere { (ConversationMemberEntity.conversationID eq conversationID) and (ConversationMemberEntity.userID eq userID) }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun readAllByConversation(conversationID: Long): List<ConversationMemberDTO> {
        return try {
            transaction {
                ConversationMemberEntity.select { ConversationMemberEntity.conversationID eq conversationID}
                    .map { mapGroupMembers(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun check(conversationID: Long, userID: Long): Boolean {
        return try {
            !transaction {
                ConversationMemberEntity.select { (ConversationMemberEntity.conversationID eq conversationID) and (ConversationMemberEntity.userID eq userID) }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }

    override suspend fun readAllByUser(userID: Long): List<ConversationMemberDTO> {
        return try {
            transaction {
                ConversationMemberEntity.select { ConversationMemberEntity.userID eq userID }
                    .map { mapGroupMembers(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun read(userID: Long, conversationID: Long): ConversationMemberDTO? {
        return try {
            transaction {
                ConversationMemberEntity.select { (ConversationMemberEntity.conversationID eq conversationID) and (ConversationMemberEntity.userID eq userID) }.map { mapGroupMembers(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun update(userID: Long, conversationID: Long, admin: Boolean): HttpStatusCode {
        return try {
            transaction {
                ConversationMemberEntity.update({ (ConversationMemberEntity.conversationID eq conversationID) and (ConversationMemberEntity.userID eq userID) }) {
                    it[this.admin] = admin
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapGroupMembers(it: ResultRow): ConversationMemberDTO {
        return ConversationMemberDTO(
            conversationID = it[ConversationMemberEntity.conversationID],
            admin = it[ConversationMemberEntity.admin],
            userID = it[ConversationMemberEntity.userID])
    }
}