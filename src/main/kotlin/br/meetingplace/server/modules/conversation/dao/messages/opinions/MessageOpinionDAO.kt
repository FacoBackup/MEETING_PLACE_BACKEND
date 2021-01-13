package br.meetingplace.server.modules.conversation.dao.messages.opinions

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageOpinionsDTO
import br.meetingplace.server.modules.conversation.entities.messages.MessageOpinionEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageOpinionDAO:MOI {
    override fun create(messageID: String, userID: String, liked: Boolean): HttpStatusCode {
        return try {
            transaction {
                MessageOpinionEntity.insert {
                    it[this.messageID] = messageID
                    it[this.userID] = userID
                    it[this.liked] = liked
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun delete(messageID: String, userID: String): HttpStatusCode {
        return try {
            transaction {
                MessageOpinionEntity.deleteWhere {
                    (MessageOpinionEntity.messageID eq messageID) and
                    (MessageOpinionEntity.userID eq userID)
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun read(messageID: String, userID: String): MessageOpinionsDTO? {
        return try {
            transaction {
                MessageOpinionEntity.select {
                (MessageOpinionEntity.messageID eq messageID)  and (MessageOpinionEntity.userID eq userID)
                }.map { mapMessageOpinions(it) }.firstOrNull()
            }
        }
        catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun update(messageID: String, userID: String, liked: Boolean): HttpStatusCode {
       return try {
           transaction {
               MessageOpinionEntity.update({ (MessageOpinionEntity.messageID eq messageID) and (MessageOpinionEntity.userID eq userID) }){
                    it[this.liked] = liked
               }
           }
           HttpStatusCode.OK
       }catch (normal: Exception){
           HttpStatusCode.InternalServerError
       }catch (psql: PSQLException){
           HttpStatusCode.InternalServerError
       }
    }

    private fun mapMessageOpinions(it: ResultRow): MessageOpinionsDTO {
        return MessageOpinionsDTO(liked = it[MessageOpinionEntity.liked],
                                  messageID = it[MessageOpinionEntity.messageID],
                                  userID = it[MessageOpinionEntity.userID])
    }
}