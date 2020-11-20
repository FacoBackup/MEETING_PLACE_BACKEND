package br.meetingplace.server.db.topic

import br.meetingplace.server.modules.topic.dto.Topic

interface TopicDBInterface {
    fun select(id: String, mainTopic: String?): Topic?
    fun insert(data: Topic)
    fun delete(data: Topic)
}