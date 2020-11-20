package br.meetingplace.server.modules.topic.dto.dependencies.sub

import br.meetingplace.server.modules.topic.dto.SimplifiedTopic
import br.meetingplace.server.modules.topic.dto.dependencies.owner.TopicOwner

class SubTopic private constructor() : SubTopicInterface {
    companion object {
        private val Class = SubTopic()
        fun getClass() = Class
    }

    private var subTopicIDs = mutableListOf<SimplifiedTopic>()

    override fun getSubTopics() = subTopicIDs

    override fun addSubTopic(subTopicID: String, subTopicOwner: TopicOwner) {
        subTopicIDs.add(SimplifiedTopic(subTopicID, subTopicOwner))
    }

    override fun removeSubTopic(subTopic: SimplifiedTopic) {
        subTopicIDs.remove(subTopic)
    }
}