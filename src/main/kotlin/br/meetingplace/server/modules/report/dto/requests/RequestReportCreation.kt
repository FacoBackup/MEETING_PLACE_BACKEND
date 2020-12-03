package br.meetingplace.server.modules.report.dto.requests

data class RequestReportCreation(val reason: String?, val communityID: String, val topicID: String, val userID: String)
