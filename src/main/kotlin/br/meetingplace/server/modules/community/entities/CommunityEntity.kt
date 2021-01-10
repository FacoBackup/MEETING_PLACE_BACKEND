package br.meetingplace.server.modules.community.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object CommunityEntity: Table("communities"){
    val id = varchar("community_id", 36)
    val name = text("community_name")
    val about = text("community_about").nullable()
    val imageURL = text("community_image_url").nullable()
    val backgroundImageURL = text("community_background_image_url").nullable()
    val creationDate = long("creation_date")
    val relatedCommunityID = varchar("parent_community_id", 36).references(id, onDelete = ReferenceOption.SET_NULL).nullable()
    override val primaryKey = PrimaryKey(id)
}