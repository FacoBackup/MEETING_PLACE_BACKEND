package br.meetingplace.server.modules.topic.dao.tag

import br.meetingplace.server.modules.topic.entities.tags.TopicTagEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicTagDAO:TTI {
    override suspend fun create(topicID: Long, tagID: Long): HttpStatusCode {
        return try{
            transaction{
                TopicTagEntity.insert {
                    it[this.topicID] = topicID
                    it[this.tagID] = tagID
                }
            }
            HttpStatusCode.Created
        }catch (e : Exception){
            HttpStatusCode.InternalServerError
        }catch (PSQL: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun delete(topicID: Long): HttpStatusCode {
        return try{
            transaction{
                TopicTagEntity.deleteWhere {
                    (TopicTagEntity.topicID eq topicID)
                }
            }
            HttpStatusCode.OK
        }catch (e : Exception){
            HttpStatusCode.InternalServerError
        }catch (PSQL: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
}