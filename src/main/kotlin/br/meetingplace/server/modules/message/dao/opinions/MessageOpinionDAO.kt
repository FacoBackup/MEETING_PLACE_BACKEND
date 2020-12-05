package br.meetingplace.server.modules.message.dao.opinions

import br.meetingplace.server.modules.message.dto.response.MessageOpinionsDTO
import br.meetingplace.server.modules.message.entities.MessageOpinion
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageOpinionDAO:MOI {
    override fun create(messageID: String, userID: String, liked: Boolean): HttpStatusCode {
        return try {
            transaction {
                MessageOpinion.insert {
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
                MessageOpinion.deleteWhere {
                    (MessageOpinion.messageID eq messageID) and
                    (MessageOpinion.userID eq userID)
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
                MessageOpinion.select {
                (MessageOpinion.messageID eq messageID)  and (MessageOpinion.userID eq userID)
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
               MessageOpinion.update({ (MessageOpinion.messageID eq messageID) and (MessageOpinion.userID eq userID) }){
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
        return MessageOpinionsDTO(liked = it[MessageOpinion.liked],
                                  messageID = it[MessageOpinion.messageID],
                                  userID = it[MessageOpinion.userID])
    }
}