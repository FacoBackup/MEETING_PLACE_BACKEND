package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO
import br.meetingplace.server.modules.topic.entities.TopicOpinionEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicOpinionDAO:TOI {
    override suspend fun check(userID: Long, topicID: Long, liked: Boolean):Boolean {
        return try {
            transaction {
                TopicOpinionEntity.select {
                    (TopicOpinionEntity.topicID eq topicID) and
                    (TopicOpinionEntity.userID eq userID) and
                    (TopicOpinionEntity.liked eq liked)
                }.firstOrNull()
            } != null

        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
    override suspend fun readQuantity(topicID: Long, likes: Boolean): Long {
        return try {
            transaction {
                TopicOpinionEntity.select {
                    (TopicOpinionEntity.topicID eq topicID) and
                            (TopicOpinionEntity.liked eq likes)
                }.count()
            }

        }catch (normal: Exception){
            0
        }catch (psql: PSQLException){
            0
        }
    }
    override suspend fun create(topicID: Long, userID: Long, liked: Boolean): HttpStatusCode {
        return try {
            transaction {
                TopicOpinionEntity.insert {
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

    override suspend fun delete(topicID: Long, userID: Long): HttpStatusCode {
        return try {
            transaction {
                TopicOpinionEntity.deleteWhere {
                    (TopicOpinionEntity.topicID eq topicID) and
                    (TopicOpinionEntity.userID eq userID)
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun read(topicID: Long, userID: Long): TopicOpinionDTO? {
        return try {
            transaction {
                TopicOpinionEntity.select {
                    (TopicOpinionEntity.topicID eq topicID) and
                    (TopicOpinionEntity.userID eq userID)
                }.map { mapTopicOpinions(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override suspend fun readAll(topicID: Long): List<TopicOpinionDTO> {
        return try {
            transaction {
                TopicOpinionEntity.select {
                    TopicOpinionEntity.topicID eq topicID
                }.map { mapTopicOpinions(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override suspend fun update(topicID: Long, userID: Long, liked: Boolean): HttpStatusCode {
        return try {
            transaction {
                TopicOpinionEntity.update({
                    (TopicOpinionEntity.topicID eq topicID) and
                 (TopicOpinionEntity.userID eq userID)
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
        return TopicOpinionDTO(liked =  it[TopicOpinionEntity.liked], userID =  it[TopicOpinionEntity.userID], topicID = it[TopicOpinionEntity.topicID])
    }
}