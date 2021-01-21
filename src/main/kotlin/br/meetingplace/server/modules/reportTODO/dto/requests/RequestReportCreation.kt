package br.meetingplace.server.modules.reportTODO.dto.requests

data class RequestReportCreation(val reason: String?,
                                 val communityID: Long,
                                 val topicID: Long)
