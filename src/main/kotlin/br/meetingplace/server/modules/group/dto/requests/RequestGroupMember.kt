package br.meetingplace.server.modules.group.dto.requests

data class RequestGroupMember(val memberID: String, val groupID: String, val userID: String)