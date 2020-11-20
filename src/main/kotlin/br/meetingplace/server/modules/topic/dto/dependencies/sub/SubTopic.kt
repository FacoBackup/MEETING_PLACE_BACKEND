package br.meetingplace.server.modules.topic.dto.dependencies.sub

import br.meetingplace.server.modules.topic.dto.SimplifiedTopic
import br.meetingplace.server.modules.topic.dto.dependencies.owner.TopicOwner

class SubTopic private constructor() : SubTopicInterface {
    companion object {
        private val Class = SubTopic()
        fun getClass() = Class
    }

    private var subTopics = mutableListOf<SimplifiedTopic>()

    override fun getSubTopics() = subTopics

    override fun addSubTopic(subTopicID: String, subTopicOwner: TopicOwner) {
        subTopics.add(SimplifiedTopic(subTopicID, subTopicOwner))
    }

    override fun removeSubTopic(id: String) {
        for (i in 0 until subTopics.size) {
            if(subTopics[i].ID == id)
                subTopics.removeAt(i)
        }
    }
}