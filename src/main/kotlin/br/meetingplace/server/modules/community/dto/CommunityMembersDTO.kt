package br.meetingplace.server.modules.community.dto

import org.jetbrains.exposed.sql.Table

data class CommunityMembersDTO(val communityID: String, var admin: Boolean, val userID: String): Table()