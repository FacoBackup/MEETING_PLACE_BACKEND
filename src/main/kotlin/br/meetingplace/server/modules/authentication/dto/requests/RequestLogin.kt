package br.meetingplace.server.modules.authentication.dto.requests

data class RequestLogin (val input: String,val ip: String, val password: String)