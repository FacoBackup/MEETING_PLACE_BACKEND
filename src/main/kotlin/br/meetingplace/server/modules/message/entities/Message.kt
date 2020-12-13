package br.meetingplace.server.modules.message.entities

import br.meetingplace.server.modules.group.entities.Group
import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Message: Table("messages"){
    var content = varchar("message_content", 4096)
    val imageURL = varchar("image_url", 256).nullable()
    val id = varchar("message_id", 36)
    val creatorID = varchar("creator_id", 36).references(User.email, onDelete = ReferenceOption.SET_NULL)
    val userReceiverID = varchar("receiver_id", 36).references(User.email, onDelete = ReferenceOption.SET_NULL).nullable()
    val groupReceiverID = varchar("group_id", 36).references(Group.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val type = short("message_type")
    val valid = integer("valid_until")
    val read = bool("read")

    override val primaryKey = PrimaryKey(id)
}