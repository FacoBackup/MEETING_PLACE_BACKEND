package br.meetingplace.server.modules.reportTODO.entities

import br.meetingplace.server.modules.community.entities.CommunityEntity
import br.meetingplace.server.modules.topic.entities.TopicEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Report: Table("reports"){
        val id = varchar("report_id", 36)
        val creatorID = varchar("creator_id", 320).references(UserEntity.email,onDelete = ReferenceOption.CASCADE)
        val topicID = varchar("topic_id", 36).references(TopicEntity.id,onDelete = ReferenceOption.CASCADE)
        val reason = text("reason").nullable()
        val communityID = varchar("community_id", 36).references(CommunityEntity.id,onDelete = ReferenceOption.CASCADE)
        val creationDate  = datetime("date_of_creation")
        var done = bool("status")
        var response = text("response").references(UserEntity.email).nullable()
        override val primaryKey = PrimaryKey(id)
}
