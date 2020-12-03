package br.meetingplace.server.modules.group.dto.response

data class GroupMemberDTO(val groupID: String, var admin: Boolean, val userID: String)
