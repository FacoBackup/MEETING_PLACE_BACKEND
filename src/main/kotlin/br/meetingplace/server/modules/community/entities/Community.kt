package br.meetingplace.server.modules.community.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object Community: Table("communities"){
    val id = varchar("community_id", 36)
    var name = varchar("community_name", 64)
    var about = varchar("community_about", 256).nullable()
    var imageURL= varchar("community_image_url", 256).nullable()
    val creationDate = datetime("date_of_creation")
    var location = varchar("location",128)
    var parentCommunityID = varchar("parent_community_id", 36).references(Community.id, onDelete = ReferenceOption.SET_NULL).nullable()
    override val primaryKey = PrimaryKey(id)
}