package br.meetingplace.server.modules.topic.dao.seen

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageStatusDTO
import br.meetingplace.server.modules.conversation.entities.messages.MessageStatusEntity
import br.meetingplace.server.modules.topic.dto.response.TopicStatusDTO
import br.meetingplace.server.modules.topic.entities.TopicStatusEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object TopicStatusDAO: TVI {
    override suspend fun create(userID: String, topicID: String): HttpStatusCode {
        return try{
            transaction{
                TopicStatusEntity.insert{
                    it[this.userID] = userID
                    it[this.topicID] = topicID
                    it[this.seenAt] = System.currentTimeMillis()
                    it[this.seen] = true
                }
            }
            HttpStatusCode.Created
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
//
//    override suspend fun update(topicID: String, userID: String): HttpStatusCode {
//        return try{
//            transaction{
//                TopicStatusEntity.update({(TopicStatusEntity.userID eq userID) and (TopicStatusEntity.topicID eq topicID)}){
//
//                    it[this.seenAt] = System.currentTimeMillis()
//                    it[this.seen] = true
//                }
//            }
//            HttpStatusCode.Created
//        }catch (e: Exception){
//            HttpStatusCode.InternalServerError
//        }catch (psql: PSQLException){
//            HttpStatusCode.InternalServerError
//        }
//    }
    override suspend fun check(topicID: String, userID: String): Boolean {
        return try{
            transaction{
                TopicStatusEntity.select{
                    (TopicStatusEntity.topicID eq topicID) and
                    (TopicStatusEntity.userID eq userID)
                }.firstOrNull()
            } != null

        }catch (e: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
//
//    override suspend fun readAllUnseenTopics(userID: String): List<TopicStatusDTO> {
//        return try{
//            transaction{
//                TopicStatusEntity.select{
//                    (TopicStatusEntity.userID eq userID) and
//                    (TopicStatusEntity.seen eq false)
//                }.map { mapTopicStatus(it) }
//            }
//        }catch (e: Exception){
//            listOf()
//        }catch (psql: PSQLException){
//            listOf()
//        }
//    }
//    private fun mapTopicStatus(it: ResultRow): TopicStatusDTO {
//        return TopicStatusDTO(
//            topicID = it[TopicStatusEntity.topicID],
//            userID = it[TopicStatusEntity.userID],
//            seen = it[TopicStatusEntity.seen],
//            seenAt = it[TopicStatusEntity.seenAt]
//        )
//    }
}