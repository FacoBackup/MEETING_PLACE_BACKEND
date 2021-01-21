package br.meetingplace.server.modules.authentication.dto.requests

data class RequestLog(val userID: Long, val ip: String, val password: String)
