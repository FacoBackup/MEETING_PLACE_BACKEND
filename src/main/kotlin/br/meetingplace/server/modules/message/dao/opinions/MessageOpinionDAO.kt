package br.meetingplace.server.modules.message.dao.opinions

import br.meetingplace.server.modules.message.dto.response.MessageOpinionsDTO
import br.meetingplace.server.modules.message.entities.MessageOpinion
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MessageOpinionDAO:MOI {
    override fun create(messageID: String, userID: String, liked: Boolean): Status {
        return try {
            transaction {
                MessageOpinion.insert {
                    it[this.messageID] = messageID
                    it[this.userID] = userID
                    it[this.liked] = liked
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(messageID: String, userID: String): Status {
        return try {
            transaction {
                MessageOpinion.deleteWhere {
                    (MessageOpinion.messageID eq messageID) and
                    (MessageOpinion.userID eq userID)
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
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

    override fun update(messageID: String, userID: String, liked: Boolean): Status {
       return try {
           transaction {
               MessageOpinion.update({ (MessageOpinion.messageID eq messageID) and (MessageOpinion.userID eq userID) }){
                    it[this.liked] = liked
               }
           }
           Status(200, StatusMessages.OK)
       }catch (normal: Exception){
           Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
       }catch (psql: PSQLException){
           Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
       }
    }

    private fun mapMessageOpinions(it: ResultRow): MessageOpinionsDTO {
        return MessageOpinionsDTO(liked = it[MessageOpinion.liked],
                                  messageID = it[MessageOpinion.messageID],
                                  userID = it[MessageOpinion.userID])
    }
}