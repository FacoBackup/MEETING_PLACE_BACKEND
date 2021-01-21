package br.meetingplace.server.modules.topic.dto.response

data class TopicOpinionDTO(val liked: Boolean, val userID: Long, val topicID: Long)
