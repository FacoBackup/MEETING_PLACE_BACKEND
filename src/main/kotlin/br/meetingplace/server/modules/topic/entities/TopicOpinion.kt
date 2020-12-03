package br.meetingplace.server.modules.topic.entities

import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicOpinion: Table("topic_opinions") {
    var liked = bool("liked")
    val userID = varchar("user_id", 36).references(User.email, onDelete = ReferenceOption.CASCADE)
    val topicID= varchar("topic_id", 36).references(Topic.id, onDelete = ReferenceOption.CASCADE)
}