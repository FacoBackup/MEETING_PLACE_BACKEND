package br.meetingplace.server.modules.group.entities

import br.meetingplace.server.modules.community.entities.Community
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object Group: Table("group"){
    val communityID = varchar("community_id", 36).references(Community.id,onDelete = ReferenceOption.CASCADE).nullable()
    val id= varchar("group_id", 36)
    var name= varchar("group_name", 64)
    var about= varchar("group_about", 256).nullable()
    var imageURL = varchar("group_image_url", 256).nullable()
    var approved = bool("approved")
    val creationDate = datetime("date_of_creation")

    override val primaryKey = PrimaryKey(id)
}
