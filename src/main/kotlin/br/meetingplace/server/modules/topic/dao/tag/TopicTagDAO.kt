package br.meetingplace.server.modules.topic.dao.tag

import br.meetingplace.server.modules.topic.dto.response.TopicTagDTO
import br.meetingplace.server.modules.topic.entities.TopicTagEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicTagDAO: TTI {
    override suspend fun create(tag: String): HttpStatusCode {
        return try {
            transaction {
                TopicTagEntity.insert {
                    it[this.numberOfTopics] = 1
                    it[this.tag] = tag
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (PSQL: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun update(tagID: Long, rankUp: Boolean): HttpStatusCode {
        return try {
            transaction {
                TopicTagEntity.update({TopicTagEntity.tagID eq tagID}) {
                    it[this.numberOfTopics] = if(rankUp) +1 else -1
                }
            }
            HttpStatusCode.OK
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (PSQL: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun read(tag: String): TopicTagDTO? {
        return try {
            transaction {
                TopicTagEntity.select{TopicTagEntity.tag like "%$tag%"}.map{ mapTopicTagDTO(it)}.firstOrNull()
            }
        }catch (e: Exception){
            null
        }catch (PSQL: PSQLException){
            null
        }
    }
    private fun mapTopicTagDTO(it: ResultRow): TopicTagDTO{
        return TopicTagDTO(
            numberOfTopics =it[TopicTagEntity.numberOfTopics],
            tagID = it[TopicTagEntity.tagID],
            tagValue = it[TopicTagEntity.tag],
        )
    }
}