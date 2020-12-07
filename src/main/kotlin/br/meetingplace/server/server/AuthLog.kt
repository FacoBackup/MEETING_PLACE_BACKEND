package br.meetingplace.server.server

import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import io.ktor.application.*
import io.ktor.auth.*
object AuthLog {
    val ApplicationCall.log get() = authentication.principal<AccessLogDTO>()
}

