package br.meetingplace.server.requests.community

data class ReportCreationData(val reason: String?, val communityID: String, val serviceID: String, val userID: String)
