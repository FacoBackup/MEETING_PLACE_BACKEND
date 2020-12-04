package br.meetingplace.server.modules.topic.dao.opinion

import br.meetingplace.server.modules.topic.dto.response.TopicOpinionDTO

interface TOI {
    fun create(topicID: String, userID: String, liked: Boolean): Status
    fun readAll(topicID: String): List<TopicOpinionDTO>
    fun read(topicID: String, userID: String): TopicOpinionDTO?
    fun update(topicID: String, userID: String, liked: Boolean): Status
    fun delete(topicID: String, userID: String): Status
}