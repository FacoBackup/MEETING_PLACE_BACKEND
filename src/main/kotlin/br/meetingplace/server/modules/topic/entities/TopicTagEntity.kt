package br.meetingplace.server.modules.topic.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicTagEntity: Table("topic_tag") {
    val tagID = long("tag_pk").autoIncrement()
    val tag = text("tag_value")
    val numberOfTopics = long("tag_rank")
    val creationDate = long("date_of_creation")
    override val primaryKey = PrimaryKey(tagID)
}