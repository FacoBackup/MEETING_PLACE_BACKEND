package br.meetingplace.server.modules.topic.dao.tag

import br.meetingplace.server.modules.topic.dto.response.TopicTagDTO
import io.ktor.http.*

interface TagInterface {
    suspend fun create(tag: String): Long?
    suspend fun read(tag: String): TopicTagDTO?
    suspend fun readRank(): List<TopicTagDTO>
}