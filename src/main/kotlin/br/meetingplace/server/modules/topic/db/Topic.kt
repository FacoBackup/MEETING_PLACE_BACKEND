package br.meetingplace.server.modules.topic.classes


import br.meetingplace.server.modules.user.classes.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date

object Topic: Table("topic") {

    var id = varchar("topic_id", 32)
    var header = varchar("header", 1024)
    var body= varchar("body", 1024)
    var approved = Boolean
    val footer= varchar("footer", 128)
    val creatorID = varchar("creator_id",32).references(User.id)
    val mainTopicID = varchar("main_topic_id",32).references(Topic.id, onDelete = ReferenceOption.CASCADE)
    val creationDate = date("date_of_creation")
    override val primaryKey = PrimaryKey(id)
}