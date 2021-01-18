package br.meetingplace.server.modules.topic.dao.topic

import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import io.ktor.http.*

interface TI {
    suspend fun read(topicID: String): TopicDTO?
    suspend fun create(header: String,
               body: String,
               imageURL: String?,
               communityID: String?,
               userID:String,
               mainTopicID: String?,
               approved:Boolean,
               userName: String): HttpStatusCode
    suspend fun readTopicsQuantityByUser(userID: String): Long
    suspend fun readTopicsQuantityByCommunity(communityID: String): Long
    suspend fun check(topicID: String): Boolean
    suspend fun readBySubject(subjectID: String,timePeriod: Long, community: Boolean): List<TopicDTO>

    suspend fun readAllComments(topicID: String): List<TopicDTO>
    suspend fun readByTimePeriod(subjectID: String, since: Long, community: Boolean): List<TopicDTO>
    suspend fun update(topicID: String,
               approved: Boolean?,
               header: String?,
               body: String?): HttpStatusCode
    suspend fun delete(topicID: String): HttpStatusCode
}