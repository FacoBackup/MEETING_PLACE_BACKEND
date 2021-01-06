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

    override fun create(data: RequestConversationCreation, id: String): HttpStatusCode {
        return try {

            transaction {
                ConversationEntity.insert {
                    it[this.id] = id
                    it[creationDate] = DateTime.now()
                    it[imageURL] = data.imageURL
                    it[about] = data.about
                    it[name] = data.name
                    it[isGroup] = data.isGroup
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){

            HttpStatusCode.InternalServerError
        }
    }

    override fun check(conversationID: String): Boolean {
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

    override fun readByName(input: String, userID: String): List<ConversationDTO> {
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
    override fun delete(conversationID: String): HttpStatusCode {
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

    override fun read(conversationID: String): ConversationDTO? {
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

    override fun update(conversationID: String, name: String?, about: String?, imageURL: String?): HttpStatusCode {
        return try {
            transaction {
                ConversationEntity.update({ ConversationEntity.id eq conversationID}) {
                    if(!name.isNullOrBlank())
                        it[this.name] = name
                    if(!about.isNullOrBlank())
                        it[this.about] = about
                    if(!imageURL.isNullOrBlank())
                        it[this.imageURL] = imageURL
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
        return ConversationDTO( id = it[ConversationEntity.id],
            name = it[ConversationEntity.name],
            imageURL = it[ConversationEntity.imageURL],
            creationDate = it[ConversationEntity.creationDate].toString("dd-MM-yyyy"),
            about = it[ConversationEntity.about],
            isGroup = it[ConversationEntity.isGroup])
    }

}