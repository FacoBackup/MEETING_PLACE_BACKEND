package br.meetingplace.server.routers.authentication

import br.meetingplace.server.modules.authentication.dao.AuthenticationDAO
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.authentication.services.AuthenticationService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*


fun Route.authentication(){
    route("/api"){
        post<RequestLog>("/login") {
            call.respond(AuthenticationService.login(it, UserDAO, AuthenticationDAO))
        }
        post<RequestLog>("/logout"){
            call.respond(AuthenticationService.logout(it, UserDAO, AuthenticationDAO))
        }
    }
}