package br.meetingplace.server.requests.report

data class RequestReportCreation(val reason: String?, val communityID: String, val topicID: String, val userID: String)
