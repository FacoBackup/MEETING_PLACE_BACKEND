package br.meetingplace.server.modules.group.dto

data class GroupMembersDTO(val groupID: String,var admin: Boolean, val userID: String)
