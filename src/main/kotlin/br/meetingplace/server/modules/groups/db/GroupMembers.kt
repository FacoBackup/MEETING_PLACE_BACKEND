package br.meetingplace.server.modules.groups.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.Table

object GroupMembers: Table("group_members") {
    val groupID = varchar("group_id", 32).references(Group.id)
    var admin = bool("is_admin")
    val userID = varchar("user_id", 32).references(User.id)
}