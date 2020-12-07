package br.meetingplace.server.modules.reportTODO.dto.requests

data class RequestReportCreation(val reason: String?, val communityID: String, val topicID: String, val userID: String)
