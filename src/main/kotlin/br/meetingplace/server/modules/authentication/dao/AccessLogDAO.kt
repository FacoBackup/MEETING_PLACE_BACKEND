package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import br.meetingplace.server.modules.authentication.entities.AccessLogEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException


object AccessLogDAO: ALI {
    override suspend fun updateOnlineStatus(machineIp: String, userID: Long): HttpStatusCode {
        return try {
            transaction {
                AccessLogEntity.update({(AccessLogEntity.machineIp eq machineIp) and  (AccessLogEntity.userID eq userID) }) {
                    it[online] = true
                    it[latestRequest] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun create(ip: String): HttpStatusCode {
        return try {
            transaction {
                AccessLogEntity.insert {
                    it[this.machineIp] = ip
                    it[online] = true
                    it[timeOfSignIn] = System.currentTimeMillis()
                    it[numberOfRequests] = 1
                    it[latestRequest] = System.currentTimeMillis()
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun delete(userID: Long, ip: String): HttpStatusCode {
        return try {
            transaction {
                AccessLogEntity.deleteWhere { (AccessLogEntity.machineIp eq ip) and (AccessLogEntity.userID eq userID)}
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun read(userID: Long, ip: String): AccessLogDTO? {
        return try {
            transaction {
                AccessLogEntity.select {
                    (AccessLogEntity.userID eq userID) and
                            (AccessLogEntity.machineIp eq ip)
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
            online = it[AccessLogEntity.online],
            Ip = it[AccessLogEntity.machineIp]
            )
    }
}