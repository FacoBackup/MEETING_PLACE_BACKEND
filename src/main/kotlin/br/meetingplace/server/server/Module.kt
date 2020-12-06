package br.meetingplace.server.server

import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.routers.authentication.authentication
import br.meetingplace.server.routers.chat.messageRouter
import br.meetingplace.server.routers.community.communityRouter
import br.meetingplace.server.routers.groups.groupRouter
import br.meetingplace.server.routers.topics.topicRouter
import br.meetingplace.server.routers.user.userRouter
import br.meetingplace.server.settings.jwt.JWTSettings
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
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
                if(userID != null)
                    UserDAO.read(userID)
                else null
            }
        }
    }
    install(Routing){
        authentication()

        authenticate(optional = true){
            userRouter()
        }
        authenticate(optional = false){
            topicRouter()
            communityRouter()
            groupRouter()
            messageRouter()
        }
    }
}