package br.meetingplace.server.user.classes.dependencies.services.topic

interface UserTopicsInterface {
    fun updateMyTopics(topic: String, add: Boolean)
    fun getMyTopics(): List<String>
}