package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO
import br.meetingplace.server.modules.topic.entities.TopicOpinion
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicOpinionDAO:TOI {

    override fun create(topicID: String, userID: String, liked: Boolean): HttpStatusCode {
        return try {
            transaction {
                TopicOpinion.insert {
                    it[this.topicID]  = topicID
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

    override fun delete(topicID: String, userID: String): HttpStatusCode {
        return try {
            transaction {
                TopicOpinion.deleteWhere {
                    (TopicOpinion.topicID eq topicID) and
                    (TopicOpinion.userID eq userID)
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun read(topicID: String, userID: String): TopicOpinionDTO? {
        return try {
            transaction {
                TopicOpinion.select {
                    (TopicOpinion.topicID eq topicID) and
                    (TopicOpinion.userID eq userID)
                }.map { mapTopicOpinions(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override fun readAll(topicID: String): List<TopicOpinionDTO> {
        return try {
            transaction {
                TopicOpinion.select {
                    TopicOpinion.topicID eq topicID
                }.map { mapTopicOpinions(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override fun update(topicID: String, userID: String, liked: Boolean): HttpStatusCode {
        return try {
            transaction {
                TopicOpinion.update({
                    (TopicOpinion.topicID eq topicID) and
                 (TopicOpinion.userID eq userID)
                }){
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
    private fun mapTopicOpinions(it: ResultRow): TopicOpinionDTO {
        return TopicOpinionDTO(liked =  it[TopicOpinion.liked], userID =  it[TopicOpinion.userID], topicID = it[TopicOpinion.topicID])
    }
}