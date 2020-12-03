package br.meetingplace.server.modules.topic.dao

import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import br.meetingplace.server.response.status.Status

interface TI {
    fun create(data: RequestTopicCreation, approved:Boolean, userName: String): Status
    fun read(topicID: String): TopicDTO?
    fun update(topicID: String,approved: Boolean?,mainTopicID: String?, header: String?, body: String?): Status
    fun delete(topicID: String): Status
}