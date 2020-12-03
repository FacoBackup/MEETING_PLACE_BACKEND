package br.meetingplace.server.modules.messageTODO.entities

import br.meetingplace.server.modules.group.entities.Group
import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Message: Table("message"){
    var content = varchar("message_content", 1024)
    val imageURL = varchar("image_url", 256).nullable()
    val id = varchar("message_id", 36)
    val creatorID = varchar("creator_id", 36).references(User.id, onDelete = ReferenceOption.SET_NULL)
    val userReceiverID = varchar("receiver_id", 36).references(User.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val groupReceiverID = varchar("group_id", 36).references(Group.id, onDelete = ReferenceOption.SET_NULL).nullable()
    val type = short("message_type")
    val creationDate = datetime("date_of_creation")

    override val primaryKey = PrimaryKey(id)
}