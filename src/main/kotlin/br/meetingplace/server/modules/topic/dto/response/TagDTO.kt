package br.meetingplace.server.modules.topic.dto.response

data class TagDTO(
    val tagID: Long,
    val tagValue: String,
    val numberOfTopics: Long
    )
