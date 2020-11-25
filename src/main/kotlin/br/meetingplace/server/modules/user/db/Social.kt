package br.meetingplace.server.modules.user.db

import org.jetbrains.exposed.sql.Table

object Social : Table("user_followers"){
    val followerID = varchar("follower_id", 32).references(User.id)
    val followedID = varchar("followed_id", 32).references(User.id)
}