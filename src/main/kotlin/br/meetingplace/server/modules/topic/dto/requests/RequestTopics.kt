package br.meetingplace.server.modules.topic.dto.requests

data class RequestTopics(val subjectID: String,
                         val timePeriod: Long,
                         val community: Boolean)
