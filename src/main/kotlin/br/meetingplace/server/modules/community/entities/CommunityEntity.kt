package br.meetingplace.server.modules.community.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object CommunityEntity: Table("communities"){
    val id = long("community_pk").autoIncrement()
    val name = text("name")
    val about = text("about").nullable()
    val pic = text("pic").nullable()
    val background = text("background").nullable()
    val creationDate = long("date_of_creation")
    val mainCommunityID = long("main_community_pk").references(id, onDelete = ReferenceOption.SET_NULL).nullable()
    override val primaryKey = PrimaryKey(id)
}