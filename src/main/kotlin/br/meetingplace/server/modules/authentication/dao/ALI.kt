package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import io.ktor.http.*

interface ALI {
    suspend fun read(userID: Long, ip: String): AccessLogDTO?
    suspend fun create(ip: String): HttpStatusCode
    suspend fun delete(userID: Long, ip: String): HttpStatusCode
    suspend fun updateOnlineStatus(machineIp: String, userID: Long): HttpStatusCode
}