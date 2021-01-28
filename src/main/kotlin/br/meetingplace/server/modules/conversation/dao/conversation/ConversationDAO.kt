package br.meetingplace.server.modules.conversation.dao.conversation

import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationCreation
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationDTO
import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.conversation.entities.conversation.ConversationMemberEntity
import br.meetingplace.server.modules.conversation.entities.conversation.ConversationOwnersEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException

object ConversationDAO: CI {

    override suspend fun create(data: RequestConversationCreation): Long? {
        return try {

            transaction {
                ConversationEntity.insert {
                    it[creationDate] = System.currentTimeMillis()
                    it[imageURL] = data.imageURL
                    it[about] = data.about
                    it[name] = data.name
                    it[isGroup] = data.isGroup
                    it[latestMessage] = null
                } get ConversationEntity.id
            }

        }catch (normal: Exception){

           null
        }catch (psql: PSQLException){

            null
        }
    }

    override suspend fun readNewest(minID: Long, requester: Long): List<ConversationDTO> {
        return try {
            val conversations = mutableListOf<ConversationDTO>()

            conversations.addAll(transaction {
                (ConversationEntity innerJoin ConversationOwnersEntity).select {
                    ConversationEntity.id.greater(minID) and
                            (ConversationOwnersEntity.secondaryUserID.eq(requester) or ConversationOwnersEntity.primaryUserID.eq(requester))
                }.map { mapConversation(it) }
            })

            conversations.addAll(transaction {
                (ConversationEntity innerJoin ConversationMemberEntity).select {
                    ConversationEntity.id.greater(minID) and
                            ConversationMemberEntity.userID.eq(requester)
                }.map { mapConversation(it) }
            })

            conversations
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun check(conversationID: Long): Boolean {
        return try {
            !transaction {
                ConversationEntity.select { ConversationEntity.id eq conversationID }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }

    override suspend fun readByName(input: String, userID: Long): List<ConversationDTO> {
        return try {
            val conversations = mutableListOf<ConversationDTO>()

            conversations.addAll(transaction {
                (ConversationEntity innerJoin ConversationOwnersEntity).select {
                    ConversationEntity.name like "%$input%" and
                    ((ConversationOwnersEntity.primaryUserID eq userID) or
                    (ConversationOwnersEntity.secondaryUserID eq userID)) and
                    (ConversationEntity.id eq ConversationOwnersEntity.conversationID)
                }.map { mapConversation(it) }
            })

            conversations.addAll(transaction {
                (ConversationEntity innerJoin ConversationMemberEntity).select {
                    ConversationEntity.name like "%$input%" and
                    (ConversationMemberEntity.userID eq userID)  and
                    (ConversationEntity.id eq ConversationMemberEntity.conversationID)
                }.map { mapConversation(it) }
            })

            conversations
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun delete(conversationID: Long): HttpStatusCode {
        return try {
            transaction {
                ConversationEntity.deleteWhere { ConversationEntity.id eq conversationID }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun read(conversationID: Long): ConversationDTO? {
        return try {
            transaction {
                ConversationEntity.select { ConversationEntity.id eq conversationID }.map { mapConversation(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun update(conversationID: Long, latestMessage: Boolean, name: String?, about: String?, imageURL: String?): HttpStatusCode {
        return try {
            transaction {
                ConversationEntity.update({ ConversationEntity.id eq conversationID}) {
                    if(!name.isNullOrBlank())
                        it[this.name] = name
                    if(!about.isNullOrBlank())
                        it[this.about] = about
                    if(!imageURL.isNullOrBlank())
                        it[this.imageURL] = imageURL
                    if(latestMessage)
                        it[this.latestMessage] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapConversation(it: ResultRow): ConversationDTO {
        return ConversationDTO(
            id = it[ConversationEntity.id],
            name = it[ConversationEntity.name],
            imageURL = it[ConversationEntity.imageURL],
            creationDate = it[ConversationEntity.creationDate],
            about = it[ConversationEntity.about],
            isGroup = it[ConversationEntity.isGroup],
            latestMessage = it[ConversationEntity.latestMessage],
            )
    }

}