package br.meetingplace.server.modules.topic.entities

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicOpinionEntity: Table("topic_opinions") {
    var liked = bool("liked")
    val userID = long("user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val topicID= long("topic_pk").references(TopicEntity.id, onDelete = ReferenceOption.CASCADE)
}