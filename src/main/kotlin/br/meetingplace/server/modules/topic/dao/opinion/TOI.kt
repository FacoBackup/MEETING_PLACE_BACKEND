package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO
import io.ktor.http.*

interface TOI {
    suspend fun create(topicID: String, userID: String, liked: Boolean): HttpStatusCode
    suspend fun readAll(topicID: String): List<TopicOpinionDTO>
    suspend fun read(topicID: String, userID: String): TopicOpinionDTO?
    suspend fun update(topicID: String, userID: String, liked: Boolean): HttpStatusCode
    suspend fun delete(topicID: String, userID: String): HttpStatusCode
    suspend fun readQuantity(topicID: String,likes: Boolean): Long
}