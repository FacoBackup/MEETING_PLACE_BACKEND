package br.meetingplace.server.modules.authentication.dto.response

import io.ktor.auth.*

data class AccessLogDTO (val userID: Long,
                         val online: Boolean,
                         val Ip: String
                        ): Principal