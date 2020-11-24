package br.meetingplace.server.modules.topic.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.Table

object TopicOpinions: Table("topic_opinions") {
    var liked = Boolean
    val userID = varchar("user_id", 32).references(User.id)
    val topicID= varchar("topic_id", 32).references(Topic.id)
}