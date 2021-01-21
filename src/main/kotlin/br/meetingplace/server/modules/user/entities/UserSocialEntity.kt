package br.meetingplace.server.modules.user.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserSocialEntity : Table("user_followers"){
    val followerID = long("follower_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val followedID = long("followed_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
}