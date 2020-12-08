package br.meetingplace.server.server

import br.meetingplace.server.modules.authentication.dao.AccessLogDAO
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
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.module(){
    install(CORS){
        method(HttpMethod.Get)
        method(HttpMethod.Put)
        method(HttpMethod.Patch)
        method(HttpMethod.Delete)
        method(HttpMethod.Post)
        method(HttpMethod.Options)
        allowCredentials = true
        anyHost()

//        intercept(ApplicationCallPipeline.Setup){
//            if(call.request.httpMethod == HttpMethod.Options)
//        }
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
                if(!userID.isNullOrBlank() && !ip.isNullOrBlank() && log != null && log.active)
                    log
                else null
            }
        }
    }
    install(Routing){
        get("/api/test"){
            val data = call.receive<String>()
//            println("email: ${data.email}")
//            println("nome: ${data.name}")
//            println("requested")
//            data.email = "$it ok"
//            data.name = "$it ok"
            println("here")
            call.respond(data + "done")
        }
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
data class Test(var name: String, var email: String)