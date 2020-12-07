package br.meetingplace.server.modules.authentication.routes

import br.meetingplace.server.modules.authentication.dao.AccessLogDAO
import br.meetingplace.server.server.AuthLog.log
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.authentication.services.AuthenticationService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.authentication(){
    route("/api"){
        post<RequestLog>("/login") {
            val token = AuthenticationService.login(it, UserDAO, AccessLogDAO)
            if(token.isNullOrBlank())
                call.respond(HttpStatusCode.Unauthorized)
            else
                call.respond(token)
        }
        authenticate {
            post<RequestLog>("/logout"){
                val log = call.log
                if(log != null)
                    call.respond(AuthenticationService.logout(userDAO = UserDAO,
                        authenticationDAO = AccessLogDAO,
                        requester = log.userID,
                        ip = log.ipAddress))
                else
                    call.respond(HttpStatusCode.Unauthorized)
            }
        }

    }
}