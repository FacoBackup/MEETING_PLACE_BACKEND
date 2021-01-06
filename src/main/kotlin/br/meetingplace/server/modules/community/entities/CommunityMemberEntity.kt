package br.meetingplace.server.modules.community.entities

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CommunityMemberEntity: Table("community_members"){
    val communityID = varchar("community_id", 36).references(CommunityEntity.id, onDelete = ReferenceOption.CASCADE)
    var role = varchar("role", 24)
    val userID = varchar("user_id",320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)
}