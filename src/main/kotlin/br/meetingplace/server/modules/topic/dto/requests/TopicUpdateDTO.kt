package br.meetingplace.server.modules.topic.dto.requests

data class TopicUpdateDTO(
    val topicID: String,
    val body: String?,
    val header: String?
)