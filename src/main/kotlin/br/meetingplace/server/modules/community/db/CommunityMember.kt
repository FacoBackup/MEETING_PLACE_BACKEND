package br.meetingplace.server.modules.community.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.Table

object CommunityMember: Table("community_members"){
    val communityID = varchar("community_id", 32).references(Community.id)
    var admin = bool("is_admin")
    val userID = varchar("user_id",32).references(User.id)
}