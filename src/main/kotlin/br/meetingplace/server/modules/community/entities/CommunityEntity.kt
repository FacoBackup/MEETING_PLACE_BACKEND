package br.meetingplace.server.modules.community.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object CommunityEntity: Table("communities"){
    val id = varchar("community_id", 36)
    val name = varchar("community_name", 256)
    val about = varchar("community_about", 512).nullable()
    val imageURL = varchar("community_image_url", 256000).nullable()
    val backgroundImageURL = varchar("community_background_image_url", 256000).nullable()
    val creationDate = long("creation_date")
    val relatedCommunityID = varchar("parent_community_id", 36).references(id, onDelete = ReferenceOption.SET_NULL).nullable()
    override val primaryKey = PrimaryKey(id)
}