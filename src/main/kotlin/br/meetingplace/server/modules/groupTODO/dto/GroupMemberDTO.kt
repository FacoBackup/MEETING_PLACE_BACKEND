package br.meetingplace.server.modules.groupTODO.dto

data class GroupMemberDTO(val groupID: String, var admin: Boolean, val userID: String)
