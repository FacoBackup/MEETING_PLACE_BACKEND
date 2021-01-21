package br.meetingplace.server.modules.reportTODO.entities

import br.meetingplace.server.modules.community.entities.CommunityEntity
import br.meetingplace.server.modules.topic.entities.TopicEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ReportEntity: Table("reports"){
        val id = long("report_pk").autoIncrement()
        val creatorID = long("creator_pk").references(UserEntity.id,onDelete = ReferenceOption.CASCADE)
        val topicID = long("topic_pk").references(TopicEntity.id,onDelete = ReferenceOption.CASCADE)
        val reason = text("reason").nullable()
        val communityID = long("community_pk").references(CommunityEntity.id,onDelete = ReferenceOption.CASCADE)
        val creationDate  = datetime("date_of_creation")
        val done = bool("closed")
        val response = text("response").nullable()
        val responseCreator = long("response_creator_pk").references(UserEntity.id, onDelete = null).nullable()
        override val primaryKey = PrimaryKey(id)
}
