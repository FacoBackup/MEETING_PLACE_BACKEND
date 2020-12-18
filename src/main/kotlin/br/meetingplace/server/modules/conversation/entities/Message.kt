package br.meetingplace.server.modules.conversation.entities

import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Message: Table("messages"){
    var content = varchar("message_content", 4096)
    val imageURL = varchar("image_url", 256).nullable()
    val id = varchar("message_id", 36)
    val creatorID = varchar("creator_id", 320).references(User.email, onDelete = ReferenceOption.CASCADE)
    val conversationID = varchar("conversation_id", 640).references(Conversation.id, onDelete = ReferenceOption.CASCADE).nullable()
    val type = short("message_type")
    val valid = integer("valid_until")
    val read = bool("read")
    val delivered = bool("received")
    val creationDate = datetime("creation_date")

    override val primaryKey = PrimaryKey(id)
}