package br.meetingplace.server.modules.topic.entities


import br.meetingplace.server.modules.community.entities.CommunityEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicEntity: Table("topics") {

    var id = varchar("topic_id", 36)
    var header = text("header")
    var body= text("body")
    var approved = bool("approved")
    val footer= varchar("footer", 128)
    val creatorID = varchar("creator_id",320).references(UserEntity.email)
    val mainTopicID = varchar("main_topic_id",36).references(id, onDelete = ReferenceOption.CASCADE).nullable()
    val creationDate = long("creation_date")
    val communityID = varchar("community_id", 36).references(CommunityEntity.id).nullable()
    val imageURL = text("image_url").nullable()
    override val primaryKey = PrimaryKey(id)
}