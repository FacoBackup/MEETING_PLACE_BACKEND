package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import io.ktor.http.*

interface ALI {
    fun read(userID: String, ip: String): AccessLogDTO?
    fun create(userID: String, ip: String): HttpStatusCode
    fun update(userID: String, ip: String): HttpStatusCode
}