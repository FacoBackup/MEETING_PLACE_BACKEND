package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import br.meetingplace.server.modules.authentication.entities.AccessLogEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException


object AccessLogDAO: ALI {
    override suspend fun create(userID: String, ip: String): HttpStatusCode {
        return try {
            transaction {
                AccessLogEntity.insert {
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

    override suspend fun delete(userID: String, ip: String): HttpStatusCode {
        return try {
            transaction {
                AccessLogEntity.deleteWhere { (AccessLogEntity.ip eq ip) and (AccessLogEntity.userID eq userID)}
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun read(userID: String, ip: String): AccessLogDTO? {
        return try {
            transaction {
                AccessLogEntity.select {
                    (AccessLogEntity.userID eq userID) and
                            (AccessLogEntity.ip eq ip)
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
            userID = it[AccessLogEntity.userID],
            ipAddress = it[AccessLogEntity.ip],
            timeOfLogin = it[AccessLogEntity.timeOfLogin].toString(),
            active = it[AccessLogEntity.active])
    }
}