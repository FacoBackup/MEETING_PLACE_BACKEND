package br.meetingplace.server.services.topic.classes

import br.meetingplace.server.data.classes.owner.topic.TopicOwnerData
import br.meetingplace.server.services.topic.classes.dependencies.Controller

class Topic(private val owner: TopicOwnerData, private val creator: String, private var id: String, private var mainTopic: String?) : Controller() {
    fun getID() = id
    fun getCreator() = creator
    fun getOwner() = owner
    fun getMainTopic() = mainTopic
}