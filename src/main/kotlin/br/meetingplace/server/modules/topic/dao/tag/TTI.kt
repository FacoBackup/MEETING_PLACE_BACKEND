package br.meetingplace.server.modules.topic.dao.tag

import br.meetingplace.server.modules.topic.dto.response.TopicTagDTO
import io.ktor.http.*

interface TTI {
    suspend fun create(tag: String): HttpStatusCode
    suspend fun update(tagID: Long, rankUp: Boolean): HttpStatusCode
    suspend fun read(tag: String): TopicTagDTO?
}