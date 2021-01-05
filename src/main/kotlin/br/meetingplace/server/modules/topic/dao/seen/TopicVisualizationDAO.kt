package br.meetingplace.server.modules.topic.dao.seen

import br.meetingplace.server.modules.topic.entities.TopicVisualizationEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicVisualizationDAO: TVI {
    override fun create(userID: String, topicID: String): HttpStatusCode {
        return try{
            transaction{
                TopicVisualizationEntity.insert{
                    it[this.userID] = userID
                    it[this.topicID] = topicID
                    it[this.seenAt] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun check(topicID: String, userID: String): Boolean {
        return try{
            transaction{
                TopicVisualizationEntity.select{
                    (TopicVisualizationEntity.topicID eq topicID) and
                    (TopicVisualizationEntity.userID eq userID)
                }.firstOrNull()
            } != null

        }catch (e: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
}