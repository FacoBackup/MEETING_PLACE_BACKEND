package br.meetingplace.server.routers.authentication

import br.meetingplace.server.modules.authentication.dao.LogDAO
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.authentication.services.AuthenticationService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.authentication(){
    route("/api"){
        post<RequestLog>("/login") {
            val token = AuthenticationService.login(it, UserDAO, LogDAO)
            if(token.isNullOrBlank())
                call.respond(HttpStatusCode.Unauthorized)
            else
                call.respond(token)
        }
        post<RequestLog>("/logout"){
            call.respond(AuthenticationService.logout(it, UserDAO, LogDAO))
        }
    }
}