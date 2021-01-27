package br.meetingplace.server.modules.topic.dto.requests

data class RequestTopics(val subjectID: Long,
                         val maxID: Long?,
                         val community: Boolean,
                         val tagID: Long?
                         )
