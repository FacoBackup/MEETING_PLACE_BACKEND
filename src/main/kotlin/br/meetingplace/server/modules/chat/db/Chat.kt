package br.meetingplace.server.modules.chat.db
import br.meetingplace.server.modules.groups.db.Group
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date


object Chat: Table("chat"){
    val id = varchar("chat_id", 32)
    val creationDate = date("date_of_creation")
    val groupID= varchar("group_id", 32).references(Group.id).nullable()
    override val primaryKey = PrimaryKey(id)
}