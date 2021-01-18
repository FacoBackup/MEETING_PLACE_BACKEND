package br.meetingplace.server.modules.topic.entities

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicArchiveEntity: Table("topic_archive") {
    val userID = varchar("user_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)
    val topicID = varchar("topic_id", 36).references(TopicEntity.id, onDelete = ReferenceOption.CASCADE)
    val creationDate = long("creation_date")
    
}