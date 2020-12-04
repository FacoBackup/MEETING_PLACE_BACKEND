package br.meetingplace.server.modules.user.dto.response

import io.ktor.http.*
import io.ktor.http.content.*

data class AuthenticationStatusDTO(val token: String, val statusCode: HttpStatusCode)
