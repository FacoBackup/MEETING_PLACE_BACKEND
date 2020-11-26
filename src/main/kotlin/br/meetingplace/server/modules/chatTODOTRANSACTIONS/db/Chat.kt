package br.meetingplace.server.modules.chatTODOTRANSACTIONS.db
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.db.Group
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date


object Chat: Table("chat"){
    val id = varchar("chat_id", 32)
    val creationDate = date("date_of_creation")
    val groupID= varchar("group_id", 32).references(Group.id, onDelete = ReferenceOption.CASCADE).nullable()
    override val primaryKey = PrimaryKey(id)
}