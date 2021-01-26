package br.meetingplace.server.modules.topic.entities.tags

import br.meetingplace.server.modules.topic.entities.TopicEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicTagEntity: Table("topic_tags") {
    val topicID = long("topic_pk").references(TopicEntity.id, onDelete = ReferenceOption.CASCADE)
    val tagID = long("tag_pk").references(TagEntity.tagID)
}