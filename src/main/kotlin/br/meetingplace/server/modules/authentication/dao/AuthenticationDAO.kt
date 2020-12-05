package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import br.meetingplace.server.modules.authentication.entities.Log
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException


object AuthenticationDAO: AI {
    override fun create(userID: String, ip: String): HttpStatusCode {
        return try {
            transaction {
                Log.insert {
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
                Log.update({
                    (Log.userID eq userID) and
                            (Log.ip eq ip)
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

    override fun read(userID: String): AccessLogDTO? {
        return try {
            transaction {
                Log.select {
                    Log.userID eq userID
                }.map { mapLog(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    private fun mapLog(it: ResultRow): AccessLogDTO {
        return AccessLogDTO(userID = it[Log.userID], ipAddress = it[Log.ip], timeOfLogin = it[Log.timeOfLogin].toString())
    }
}