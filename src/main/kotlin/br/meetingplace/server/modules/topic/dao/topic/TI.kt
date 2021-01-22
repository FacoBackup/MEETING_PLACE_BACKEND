package br.meetingplace.server.modules.topic.dao.topic

import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import io.ktor.http.*

interface TI {
    suspend fun read(topicID: Long): TopicDTO?
    suspend fun create(
            header: String,
            body: String?,
            imageURL: String?,
            communityID: Long?,
            userID:Long,
            mainTopicID: Long?,
            approved:Boolean): Long?
    suspend fun readTopicsQuantityByUser(userID: Long): Long
    suspend fun readTopicsQuantityByCommunity(communityID: Long): Long
    suspend fun check(topicID: Long): Boolean
    suspend fun readBySubject(subjectID: Long,timePeriod: Long, community: Boolean): List<TopicDTO>
    suspend fun readByMaxID(subjectID: Long, maxID: Long, community: Boolean): List<TopicDTO>
    suspend fun readNewestBySubject(subjectID: Long, community: Boolean): List<TopicDTO>
    suspend fun readAllComments(topicID: Long): List<TopicDTO>
    suspend fun readByTimePeriod(subjectID: Long, since: Long, community: Boolean): List<TopicDTO>
    suspend fun update(topicID: Long,
               approved: Boolean?,
               header: String?,
               body: String?): HttpStatusCode
    suspend fun delete(topicID: Long): HttpStatusCode
    suspend fun readQuantityComments(topicID: Long): Long
}
