package br.meetingplace.server.modules.topic.dto.response

data class TopicStatusDTO(
    val topicID: String,
    val userID: String,
    val seen:Boolean,
    val seenAt: Long?
)
