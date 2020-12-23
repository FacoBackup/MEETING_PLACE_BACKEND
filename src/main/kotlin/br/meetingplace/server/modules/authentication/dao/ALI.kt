package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import io.ktor.http.*

interface ALI {
    suspend fun read(userID: String, ip: String): AccessLogDTO?
    suspend fun create(userID: String, ip: String): HttpStatusCode
    suspend fun delete(userID: String, ip: String): HttpStatusCode
}