package br.meetingplace.server.modules.topic.dto.dependencies.sub

interface SubTopicInterface {
    fun addSubTopic(subTopicID: String)
    fun removeSubTopic(subTopicID: String)
    fun getSubTopics(): List<String>
}