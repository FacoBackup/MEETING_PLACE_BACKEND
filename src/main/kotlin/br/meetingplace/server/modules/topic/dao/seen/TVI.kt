package br.meetingplace.server.modules.topic.dao.seen

import br.meetingplace.server.modules.topic.dto.response.TopicStatusDTO
import io.ktor.http.*

interface TVI {
    suspend fun create(userID: String, topicID: String): HttpStatusCode
    suspend fun check(topicID: String, userID: String): Boolean
//    suspend fun readAllUnseenTopics(userID: String): List<TopicStatusDTO>
//    suspend fun update(topicID: String, userID: String): HttpStatusCode
}