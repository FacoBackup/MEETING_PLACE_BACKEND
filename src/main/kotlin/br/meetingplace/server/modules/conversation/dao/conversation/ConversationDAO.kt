package br.meetingplace.server.modules.conversation.dao.conversation

import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationCreation
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationDTO
import br.meetingplace.server.modules.conversation.entities.ConversationEntity
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
                ConversationEntity.update({ConversationEntity.id eq conversationID}) {
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