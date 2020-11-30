package br.meetingplace.server.modules.topic.db


import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime

object Topic: Table("topic") {

    var id = varchar("topic_id", 36)
    var header = varchar("header", 1024)
    var body= varchar("body", 1024)
    var approved = bool("approved")
    val footer= varchar("footer", 128)
    val creatorID = varchar("creator_id",36).references(User.id)
    val mainTopicID = varchar("main_topic_id",36).references(id, onDelete = ReferenceOption.CASCADE).nullable()
    val creationDate = datetime("date_of_creation")
    val communityID = varchar("community_id", 36).references(Community.id).nullable()
    val imageURL = varchar("image_url", 256).nullable()
    override val primaryKey = PrimaryKey(id)
}