package br.meetingplace.server.modules.group.dto

data class GroupMemberDTO(val groupID: String, var admin: Boolean, val userID: String)
