package br.meetingplace.server.modules.topic.db

import br.meetingplace.server.modules.user.db.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicOpinions: Table("topic_opinions") {
    var liked = bool("liked")
    val userID = varchar("user_id", 36).references(User.id, onDelete = ReferenceOption.CASCADE)
    val topicID= varchar("topic_id", 36).references(Topic.id, onDelete = ReferenceOption.CASCADE)
}