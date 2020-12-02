package br.meetingplace.server.modules.communityTODO.dto

import org.jetbrains.exposed.sql.Table

data class CommunityMemberDTO(val communityID: String, var role: String, val userID: String): Table()