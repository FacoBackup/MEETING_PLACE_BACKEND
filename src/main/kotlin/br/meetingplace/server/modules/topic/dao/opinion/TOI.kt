package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.TopicOpinionDTO
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import br.meetingplace.server.response.status.Status

interface TOI {
    fun create(topicID: String, userID: String, liked: Boolean): Status
    fun readAll(topicID: String): List<TopicOpinionDTO>
    fun update(topicID: String, userID: String, liked: Boolean): Status
    fun delete(topicID: String, userID: String): Status
}