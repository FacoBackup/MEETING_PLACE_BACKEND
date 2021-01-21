package br.meetingplace.server.modules.community.entities

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CommunityMemberEntity: Table("community_members"){
    val communityID = long("community_pk").references(CommunityEntity.id, onDelete = ReferenceOption.CASCADE)
    var role = varchar("user_role", 24)
    val userID = long("user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
}