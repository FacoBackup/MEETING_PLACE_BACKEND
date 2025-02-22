package br.meetingplace.server.modules.authentication.entities

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object AccessLogEntity: Table("logs") {
    val userID = varchar("user_id", 320).references(UserEntity.email, onDelete = ReferenceOption.CASCADE)
    val ip = varchar("ip_address", 64)
    val active = bool("is_active")
    val timeOfLogin = datetime("date_of_login")
    override val primaryKey = PrimaryKey(ip)
}