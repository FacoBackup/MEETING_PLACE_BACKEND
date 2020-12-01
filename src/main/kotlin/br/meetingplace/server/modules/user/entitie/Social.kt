package br.meetingplace.server.modules.user.entitie

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Social : Table("user_followers"){
    val followerID = varchar("follower_id", 36).references(User.id, onDelete = ReferenceOption.CASCADE)
    val followedID = varchar("followed_id", 36).references(User.id, onDelete = ReferenceOption.CASCADE)
}