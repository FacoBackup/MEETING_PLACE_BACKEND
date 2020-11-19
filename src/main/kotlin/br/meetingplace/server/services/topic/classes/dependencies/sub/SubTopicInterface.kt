package br.meetingplace.server.services.topic.classes.dependencies.sub

interface SubTopicInterface {
    fun addSubTopic(subTopicID: String)
    fun removeSubTopic(subTopicID: String)
    fun getSubTopics(): List<String>
}