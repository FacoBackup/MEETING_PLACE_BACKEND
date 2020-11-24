package br.meetingplace.server.db.topic

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.topic.db.Topic

interface TopicDBInterface {
    fun select(id: String, mainTopic: String?): Topic?
    fun insert(data: Topic): Status
    fun delete(data: Topic): Status
    fun check(id: String): Boolean
}