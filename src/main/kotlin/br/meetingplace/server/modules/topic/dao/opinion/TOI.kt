package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO
import io.ktor.http.*

interface TOI {
    fun create(topicID: String, userID: String, liked: Boolean): HttpStatusCode
    fun readAll(topicID: String): List<TopicOpinionDTO>
    fun read(topicID: String, userID: String): TopicOpinionDTO?
    fun update(topicID: String, userID: String, liked: Boolean): HttpStatusCode
    fun delete(topicID: String, userID: String): HttpStatusCode
}