package br.meetingplace.server.modules.authentication.dto.response

import io.ktor.auth.*

data class AccessLogDTO (val userID: String,
                         val ipAddress: String,
                         val timeOfLogin: String,
                         val active: Boolean): Principal