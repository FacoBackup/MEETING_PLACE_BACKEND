package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO
import io.ktor.http.*

interface TOI {
    suspend fun create(topicID: Long, userID: Long, liked: Boolean): HttpStatusCode
    suspend fun readAll(topicID: Long): List<TopicOpinionDTO>
    suspend fun read(topicID: Long, userID: Long): TopicOpinionDTO?
    suspend fun update(topicID: Long, userID: Long, liked: Boolean): HttpStatusCode
    suspend fun delete(topicID: Long, userID: Long): HttpStatusCode
    suspend fun readQuantity(topicID: Long,likes: Boolean): Long
    suspend fun check(userID: Long, topicID: Long, liked: Boolean): Boolean
}