package br.meetingplace.server.modules.conversation.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Conversation: Table("conversation"){
    val id= varchar("id", 256)
    var name= varchar("name", 256)
    var about= varchar("about", 256).nullable()
    var imageURL = varchar("image_url", 256).nullable()
    val creationDate = datetime("created_in")
    val isGroup = bool("is_group")

    override val primaryKey = PrimaryKey(id)
}
