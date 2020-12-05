package br.meetingplace.server

import br.meetingplace.server.db.settings.Settings
import br.meetingplace.server.routers.authentication.authentication
import br.meetingplace.server.routers.chat.messageRouter
import br.meetingplace.server.routers.community.communityRouter
import br.meetingplace.server.routers.groups.groupRouter
import br.meetingplace.server.routers.topics.topicRouter
import br.meetingplace.server.routers.user.userRouter
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main() {

    val db = Settings.dbSettings(host = "localhost", dbName = "api_database", user = "api", password = "12345" )
    Settings.setUpTables()

    if(db != null){
        val port = System.getenv("PORT")?.toInt() ?: 8080
        embeddedServer(Netty, port) {
            routing {
                install(ContentNegotiation) {
                    gson {
                        setPrettyPrinting()
                    }
                }
//                install(Authentication){
//                    jwt {
//                        verifier(AuthenticationService.jwtVerifier)
//                        realm = "Intranet API"
//                        validate {
//                            val userID = it.payload.getClaim("userID").asString()
//                            val password = it.payload.getClaim("password").asString()
//                            if(userID != null && password != null){
//                                AuthenticationDAO.read(userID)
//                            }
//                            else
//                                false
//                        }
//                    }
//                }
                userRouter()
                authentication()
                topicRouter()
                communityRouter()
                groupRouter()
                messageRouter()

            }
        }.start(wait = true)
    }
}


