package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import br.meetingplace.server.modules.authentication.entities.AccessLog
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException


object AccessLogDAO: ALI {
    override fun create(userID: String, ip: String): HttpStatusCode {
        return try {
            transaction {
                AccessLog.insert {
                    it[this.ip] = ip
                    it[this.userID] = userID
                    it[active] = true
                    it[timeOfLogin] = DateTime.now()
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun update(userID: String, ip: String): HttpStatusCode {
        return try {
            transaction {
                AccessLog.update({
                    (AccessLog.userID eq userID) and
                            (AccessLog.ip eq ip)
                }){
                    it[active] = false
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun read(userID: String, ip: String): AccessLogDTO? {
        return try {
            transaction {
                AccessLog.select {
                    (AccessLog.userID eq userID) and
                            (AccessLog.ip eq ip)
                }.map { mapLog(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    private fun mapLog(it: ResultRow): AccessLogDTO {
        return AccessLogDTO(
            userID = it[AccessLog.userID],
            ipAddress = it[AccessLog.ip],
            timeOfLogin = it[AccessLog.timeOfLogin].toString(),
            active = it[AccessLog.active])
    }
}