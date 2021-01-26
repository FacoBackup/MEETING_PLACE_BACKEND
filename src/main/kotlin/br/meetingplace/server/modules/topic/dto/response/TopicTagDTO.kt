package br.meetingplace.server.modules.topic.dto.response

data class TopicTagDTO(
    val tagID: Long,
    val tagValue: String,
    val numberOfTopics: Long
    )
