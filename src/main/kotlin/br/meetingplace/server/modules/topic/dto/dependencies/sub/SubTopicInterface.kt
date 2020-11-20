package br.meetingplace.server.modules.topic.dto.dependencies.sub

import br.meetingplace.server.modules.topic.dto.SimplifiedTopic
import br.meetingplace.server.modules.topic.dto.dependencies.owner.TopicOwner

interface SubTopicInterface {
    fun addSubTopic(subTopicID: String, subTopicOwner: TopicOwner)
    fun removeSubTopic(subTopic: String)
    fun getSubTopics(): List<SimplifiedTopic>
}