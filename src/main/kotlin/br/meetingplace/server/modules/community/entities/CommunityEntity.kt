package br.meetingplace.server.modules.community.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object CommunityEntity: Table("communities"){
    val id = varchar("community_id", 36)
    var name = varchar("community_name", 256)
    var about = varchar("community_about", 512).nullable()
    var imageURL= varchar("community_image_url", 256).nullable()
    val creationDate = long("creation_date")
    var parentCommunityID = varchar("parent_community_id", 36).references(id, onDelete = ReferenceOption.SET_NULL).nullable()
    override val primaryKey = PrimaryKey(id)
}