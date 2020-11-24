package br.meetingplace.server.modules.user.classes

import org.jetbrains.exposed.sql.Table

data class UserFollowers (val followerID: String, val followedID: String): Table()