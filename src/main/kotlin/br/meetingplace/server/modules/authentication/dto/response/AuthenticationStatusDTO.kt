package br.meetingplace.server.modules.authentication.dto.response

import io.ktor.http.*
import io.ktor.http.content.*

data class AuthenticationStatusDTO(val token: String?, val statusCode: HttpStatusCode)
