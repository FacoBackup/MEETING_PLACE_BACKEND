package br.meetingplace.server.modules.topic.dto.requests

data class TopicUpdateDTO(
    val topicID: Long,
    val body: String?,
    val header: String?
)