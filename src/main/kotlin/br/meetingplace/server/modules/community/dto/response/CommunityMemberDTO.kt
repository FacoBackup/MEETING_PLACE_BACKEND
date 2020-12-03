package br.meetingplace.server.modules.community.dto.response

import org.jetbrains.exposed.sql.Table

data class CommunityMemberDTO(val communityID: String, var role: String, val userID: String): Table()