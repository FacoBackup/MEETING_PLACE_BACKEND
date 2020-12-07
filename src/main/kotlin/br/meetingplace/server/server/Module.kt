package br.meetingplace.server.server

import br.meetingplace.server.modules.authentication.dao.AccessLogDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.authentication.routes.authentication
import br.meetingplace.server.modules.message.routes.messageRouter
import br.meetingplace.server.modules.community.routes.communityRouter
import br.meetingplace.server.modules.group.routes.groupRouter
import br.meetingplace.server.modules.topic.routes.topicRouter
import br.meetingplace.server.modules.user.routes.userRouter
import br.meetingplace.server.settings.jwt.JWTSettings
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.module(){
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication){
        jwt {
            verifier(JWTSettings.jwtVerifier)
            realm = "Intranet API"
            validate {
                val userID = it.payload.getClaim("userID").asString()
                val ip = it.payload.getClaim("ip").asString()
                val log = AccessLogDAO.read(userID, ip)
                if(!userID.isNullOrBlank() && !ip.isNullOrBlank() && log != null && log.active)
                    log
                else null
            }
        }
    }
    install(Routing){

        authenticate(optional = true){
            userRouter()
            authentication()
        }
        authenticate(optional = false){
            topicRouter()
            communityRouter()
            groupRouter()
            messageRouter()
        }
    }
}