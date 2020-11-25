package br.meetingplace.server.modules.report.db

import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import java.time.LocalDateTime

object Report: Table("report"){
        val id = varchar("report_id", 32)
        val creatorID = varchar("creator_id", 32).references(User.id,onDelete = ReferenceOption.CASCADE)
        val topicID = varchar("topic_id", 32).references(Topic.id,onDelete = ReferenceOption.CASCADE)
        val reason = varchar("reason", 32).nullable()
        val communityID = varchar("community_id", 32).references(Community.id,onDelete = ReferenceOption.CASCADE)
        val creationDate  = date("date_of_creation")
        var status = short("status")
        var response = varchar("response", 32).references(User.id).nullable()
        override val primaryKey = PrimaryKey(id)
}
