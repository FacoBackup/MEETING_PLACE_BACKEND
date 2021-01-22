package br.meetingplace.server.modules.topic.dao.timeline.item

import br.meetingplace.server.modules.topic.dto.response.TimelineItemDTO
import io.ktor.http.*

interface TMII {
    suspend fun create(topicID: Long, userID: Long): HttpStatusCode
    suspend fun read(userID: Long): List<TimelineItemDTO>
    suspend fun readByMaxID(userID: Long,maxID: Long): List<TimelineItemDTO>
    suspend fun readNewest(userID: Long): List<TimelineItemDTO>
}