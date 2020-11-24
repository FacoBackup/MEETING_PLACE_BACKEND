package br.meetingplace.server.modules.community.db

import org.jetbrains.exposed.sql.Table

data class CommunityMembers(val communityID: String, var admin: Boolean, val userID: String): Table()