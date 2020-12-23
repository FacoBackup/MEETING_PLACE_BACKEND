package br.meetingplace.server.server

import br.meetingplace.server.modules.authentication.dao.AccessLogDAO
import br.meetingplace.server.modules.authentication.dto.response.AccessLogDTO
import br.meetingplace.server.modules.authentication.routes.authentication
import br.meetingplace.server.modules.community.routes.communityRouter
import br.meetingplace.server.modules.conversation.routes.groupConversationRouter
import br.meetingplace.server.modules.conversation.routes.messageRouter
import br.meetingplace.server.modules.conversation.routes.userConversationRouter
import br.meetingplace.server.modules.topic.routes.topicRouter
import br.meetingplace.server.modules.user.routes.userRouter
import br.meetingplace.server.settings.jwt.JWTSettings
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.routing.*

fun Application.module(){
    install(CORS){
        header(HttpHeaders.Authorization)
        header(HttpHeaders.AccessControlAllowOrigin)
        header(HttpHeaders.ContentType)
        anyHost()
        method(HttpMethod.Get)
        method(HttpMethod.Put)
        method(HttpMethod.Patch)
        method(HttpMethod.Delete)
        method(HttpMethod.Post)
        method(HttpMethod.Options)
        allowCredentials = true
        allowSameOrigin = true
        allowNonSimpleContentTypes = true
    }
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
                if(!userID.isNullOrBlank() && !ip.isNullOrBlank()) {
                    log ?: AccessLogDTO(userID =  userID, ipAddress = ip, timeOfLogin = System.currentTimeMillis().toString(),active = true)
                }
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
            userConversationRouter()
            messageRouter()
            groupConversationRouter()
        }
    }
}
