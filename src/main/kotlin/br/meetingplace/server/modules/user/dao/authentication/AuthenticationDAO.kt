package br.meetingplace.server.modules.user.dao.authentication

import br.meetingplace.server.modules.user.dto.response.AuthenticationDTO
import br.meetingplace.server.modules.user.entities.Log
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object AuthenticationDAO:AI {
    override fun create(userID: String): Status {
        return try {
            transaction {
                Log.insert {
                    it[loginToken] = UUID.randomUUID().toString()
                    it[this.userID] = userID
                    it[timeOfLogin] = DateTime.now()
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(userID: String): Status {
        return try {
            transaction {
                Log.deleteWhere {
                    Log.userID eq userID
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(userID: String): AuthenticationDTO? {
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
    private fun mapLog(it: ResultRow): AuthenticationDTO{
        return AuthenticationDTO(userID = it[Log.userID], token = it[Log.loginToken])
    }
}