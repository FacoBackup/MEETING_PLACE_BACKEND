package br.meetingplace.server.modules.topic.dao.seen

import br.meetingplace.server.modules.topic.dto.response.TopicStatusDTO
import io.ktor.http.*

interface TVI {
    suspend fun create(userID: Long, topicID: Long): HttpStatusCode
    suspend fun check(topicID: Long, userID: Long): Boolean
}