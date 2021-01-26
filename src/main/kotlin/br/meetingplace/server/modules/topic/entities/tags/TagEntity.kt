package br.meetingplace.server.modules.topic.entities.tags

import org.jetbrains.exposed.sql.Table

object TagEntity: Table("tag") {
    val tagID = long("tag_pk").autoIncrement()
    val tag = text("tag_value")
    val creationDate = long("date_of_creation")
    override val primaryKey = PrimaryKey(tagID)
}