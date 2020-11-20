package br.meetingplace.server.modules.topic.dto

import br.meetingplace.server.modules.topic.dto.dependencies.Controller

class Topic(private val owner: TopicOwnerData, private val creator: String, private var id: String, private var mainTopic: String?) : Controller() {
    fun getID() = id
    fun getCreator() = creator
    fun getOwner() = owner
    fun getMainTopic() = mainTopic
}