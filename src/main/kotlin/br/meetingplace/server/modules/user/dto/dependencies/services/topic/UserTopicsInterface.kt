package br.meetingplace.server.modules.user.dto.dependencies.services.topic

interface UserTopicsInterface {
    fun updateMyTopics(topic: String, add: Boolean)
    fun getMyTopics(): List<String>
}