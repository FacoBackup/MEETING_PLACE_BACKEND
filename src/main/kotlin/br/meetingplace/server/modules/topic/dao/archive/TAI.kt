package br.meetingplace.server.modules.topic.dao.archive

import br.meetingplace.server.modules.topic.dto.response.TagDTO
import br.meetingplace.server.modules.topic.dto.response.TopicTagDTO
import io.ktor.http.*

interface TAI {
    suspend fun create(topicID: Long, requester: Long): HttpStatusCode
    suspend fun check(topicID: Long, requester: Long): Boolean
    suspend fun delete(topicID: Long, requester: Long): HttpStatusCode

}