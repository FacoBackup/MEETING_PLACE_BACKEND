package br.meetingplace.server.modules.report.db

import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime

object Report: Table("report"){
        val id = varchar("report_id", 36)
        val creatorID = varchar("creator_id", 36).references(User.id,onDelete = ReferenceOption.CASCADE)
        val topicID = varchar("topic_id", 36).references(Topic.id,onDelete = ReferenceOption.CASCADE)
        val reason = varchar("reason", 32).nullable()
        val communityID = varchar("community_id", 36).references(Community.id,onDelete = ReferenceOption.CASCADE)
        val creationDate  = datetime("date_of_creation")
        var status = short("status")
        var response = varchar("response", 36).references(User.id).nullable()
        override val primaryKey = PrimaryKey(id)
}
