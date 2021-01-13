package br.meetingplace.server.modules.user.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserSocialEntity : Table("user_followers"){
    val followerID = varchar("follower_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)
    val followedID = varchar("followed_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)
}