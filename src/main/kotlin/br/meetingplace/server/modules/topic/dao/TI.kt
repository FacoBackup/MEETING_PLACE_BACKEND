package br.meetingplace.server.modules.topic.dao

import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import io.ktor.http.*

interface TI {
    fun read(topicID: String): TopicDTO?
    fun create(data: RequestTopicCreation, approved:Boolean, userName: String): HttpStatusCode
    fun check(topicID: String): HttpStatusCode
    fun readByUser(userID: String): List<TopicDTO>
    fun readAllComments(topicID: String): List<TopicDTO>
    fun update(topicID: String,
               approved: Boolean?,
               mainTopicID: String?,
               header: String?,
               body: String?): HttpStatusCode
    fun delete(topicID: String): HttpStatusCode
}