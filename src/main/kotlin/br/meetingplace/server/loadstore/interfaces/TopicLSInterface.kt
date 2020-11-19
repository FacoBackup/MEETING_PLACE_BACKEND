package br.meetingplace.server.loadstore.interfaces

import br.meetingplace.server.services.topic.classes.Topic

interface TopicLSInterface {
    fun load(id: String, mainTopic: String?): Topic?
    fun store(data: Topic)
    fun delete(data: Topic)
}