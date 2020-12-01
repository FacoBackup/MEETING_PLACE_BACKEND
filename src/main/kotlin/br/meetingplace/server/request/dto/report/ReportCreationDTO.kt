package br.meetingplace.server.request.dto.report

data class ReportCreationDTO(val reason: String?, val communityID: String, val topicID: String, val userID: String)
