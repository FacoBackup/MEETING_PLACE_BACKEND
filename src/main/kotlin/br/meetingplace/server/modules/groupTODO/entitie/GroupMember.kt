package br.meetingplace.server.modules.groupTODO.entitie

import br.meetingplace.server.modules.user.entitie.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object GroupMember: Table("group_members") {
    val groupID = varchar("group_id", 36).references(Group.id,onDelete = ReferenceOption.CASCADE)
    var admin = bool("is_admin")
    val userID = varchar("user_id", 36).references(User.id,onDelete = ReferenceOption.CASCADE)
}