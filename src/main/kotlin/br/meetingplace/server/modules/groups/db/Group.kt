package br.meetingplace.server.modules.groups.db

import br.meetingplace.server.modules.community.db.Community
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date


object Group: Table("group"){
    val communityID = varchar("community_id", 32).references(Community.id)
    val id= varchar("group_id", 32)
    var name= varchar("group_name", 64)
    var about= varchar("group_about", 256).nullable()
    var imageURL = varchar("group_image_url", 256).nullable()
    var approved = bool("approved")
    val creationDate = date("date_of_creation")

    override val primaryKey = PrimaryKey(Community.id)
}
