package br.meetingplace.server.modules.authentication.dao

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import io.ktor.http.*

interface AI {
    fun read(userID: String): AccessLogDTO?
    fun create(userID: String, ip: String): HttpStatusCode
    fun update(userID: String, ip: String): HttpStatusCode
}