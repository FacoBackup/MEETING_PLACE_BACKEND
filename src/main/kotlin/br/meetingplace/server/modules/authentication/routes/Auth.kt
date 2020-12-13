package br.meetingplace.server.modules.authentication.routes

import br.meetingplace.server.modules.authentication.dao.AccessLogDAO
import br.meetingplace.server.server.AuthLog.log
import br.meetingplace.server.modules.authentication.dto.requests.RequestLog
import br.meetingplace.server.modules.authentication.entities.AccessLog
import br.meetingplace.server.modules.authentication.services.AuthenticationService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException


fun Route.authentication(){

    route("/api"){
        authenticate {
            get("/verify/token"){
                val log = call.log
                println("Verification requested")
                if(log != null){
                    println("Token accepted")
                    call.respond(HttpStatusCode.Accepted)
                }

                else
                    call.respond(HttpStatusCode.Unauthorized)
            }
        }
        put("/login") {
            val data = call.receive<RequestLog>()
            val token = AuthenticationService.login(data, UserDAO, AccessLogDAO)
            if(token.isNullOrBlank())
                call.respond(HttpStatusCode.Unauthorized)
            else
                call.respond(token)
        }
        delete("/delete/logs"){
            try {
                transaction {
                    AccessLog.deleteAll()
                }
                call.respond(HttpStatusCode.Accepted)
            }catch (e: PSQLException){
                call.respond(HttpStatusCode.InternalServerError)
            }catch (e: Exception){
                call.respond(HttpStatusCode.InternalServerError)
            }


        }
        authenticate {
            post("/logout"){
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