package br.meetingplace.server.modules.community.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import java.time.LocalDateTime


object Community: Table("community"){

    var name = varchar("community_name", 64)
    val id = varchar("community_id", 32)
    var about = varchar("community_about", 256).nullable()
    var imageURL= varchar("community_image_url", 256).nullable()
    val creationDate = date("date_of_creation")

    override val primaryKey = PrimaryKey(id)
}