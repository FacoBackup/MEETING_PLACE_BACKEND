package br.meetingplace.server.modules.topic.dao.archive

import br.meetingplace.server.modules.topic.entities.TopicArchiveEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicArchiveDAO: TAI {
    override suspend fun create(topicID: Long, requester: Long): HttpStatusCode {
        return try{
            transaction {
                TopicArchiveEntity.insert {
                    it[this.topicID] = topicID
                    it[this.userID] = requester
                    it[this.creationDate] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun check(topicID: Long, requester: Long): Boolean {
        return try{
            transaction {
                TopicArchiveEntity.select {
                    (TopicArchiveEntity.topicID eq topicID) and (TopicArchiveEntity.userID eq requester)
                }.firstOrNull()
            } != null

        }catch (e: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }

    override suspend fun delete(topicID: Long, requester: Long): HttpStatusCode {
        return try{
            transaction {
                TopicArchiveEntity.deleteWhere {
                    (TopicArchiveEntity.topicID eq topicID) and (TopicArchiveEntity.userID eq requester)
                }
            }
            HttpStatusCode.OK
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
}