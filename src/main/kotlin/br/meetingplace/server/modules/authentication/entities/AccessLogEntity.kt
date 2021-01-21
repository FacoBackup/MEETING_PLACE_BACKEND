package br.meetingplace.server.modules.authentication.entities

import br.meetingplace.server.modules.user.entities.UserEntity
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object AccessLogEntity: Table("logs") {
    private val accessLogID = long("log_pk").autoIncrement()
    val machineIp = text("machine_ip_address")


    val userID = long("user_pk").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val online = bool("is_online")
    val timeOfSignIn = long("time_of_sign_in")
    val numberOfRequests = long("amount_of_requests")
    val latestRequest = long("latest_request")
    override val primaryKey = PrimaryKey(accessLogID)
}