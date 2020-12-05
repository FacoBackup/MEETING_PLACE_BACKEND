package br.meetingplace.server.modules.authentication.dto.response

data class AccessLogDTO (val userID: String, val ipAddress: String, val timeOfLogin: String)