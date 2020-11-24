package br.meetingplace.server.modules.user.classes

import org.jetbrains.exposed.sql.Table

data class UserGroups(val groupID: String,var admin: Boolean, val userID: String): Table()
