package br.meetingplace.server.modules.topic.dao

import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import io.ktor.http.*

interface TI {
    fun read(topicID: String): TopicDTO?
    fun create(header: String,
               body: String,
               imageURL: String?,
               communityID: String?,
               userID:String,
               mainTopicID: String?,
               approved:Boolean,
               userName: String): HttpStatusCode
    fun check(topicID: String): Boolean
    fun readByUser(userID: String): List<TopicDTO>
    fun readAllComments(topicID: String): List<TopicDTO>
    fun update(topicID: String,
               approved: Boolean?,
               mainTopicID: String?,
               header: String?,
               body: String?): HttpStatusCode
    fun delete(topicID: String): HttpStatusCode
}