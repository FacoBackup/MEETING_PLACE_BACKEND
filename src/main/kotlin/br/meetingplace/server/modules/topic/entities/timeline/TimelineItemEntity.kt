package br.meetingplace.server.modules.topic.entities.timeline

import br.meetingplace.server.modules.topic.entities.TopicEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TimelineItemEntity: Table("user_timeline_item"){
    val topicID = long("topic_pk").references(TopicEntity.id, onDelete = ReferenceOption.CASCADE)
    val userID = long("user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val validUntil = long("valid_until")
}