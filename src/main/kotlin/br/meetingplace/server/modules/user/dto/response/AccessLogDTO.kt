package br.meetingplace.server.modules.user.dto.response

data class AccessLogDTO (val userID: String, val ipAddress: String, val timeOfLogin: String)