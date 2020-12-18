package br.meetingplace.server.modules.community.entities

import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CommunityMember: Table("community_members"){
    val communityID = varchar("community_id", 36).references(Community.id, onDelete = ReferenceOption.CASCADE)
    var role = varchar("role", 24)
    val userID = varchar("user_id",320).references(User.email, onDelete = ReferenceOption.CASCADE)
}