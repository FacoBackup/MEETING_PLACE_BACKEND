package br.meetingplace.server.modules.groups.dto

data class GroupMembersDTO(val groupID: String,var admin: Boolean, val userID: String)
