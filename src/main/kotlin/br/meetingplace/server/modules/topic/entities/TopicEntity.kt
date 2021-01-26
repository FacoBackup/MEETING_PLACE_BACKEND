package br.meetingplace.server.modules.topic.entities


import br.meetingplace.server.modules.community.entities.CommunityEntity
import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TopicEntity: Table("topics") {

    var id = long("topic_pk").autoIncrement()
    var header = text("header")
    var body= text("body").nullable()
    var approved = bool("approved")
    val creatorID = long("creator_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val mainTopicID = long("main_topic_pk").references(id, onDelete = ReferenceOption.CASCADE).nullable()
    val creationDate = long("creation_date")
    val communityID = long("community_pk").references(CommunityEntity.id).nullable()
    val image = text("image").nullable()

    override val primaryKey = PrimaryKey(id)
}