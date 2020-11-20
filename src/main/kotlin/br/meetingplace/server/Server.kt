package br.meetingplace.server

import br.meetingplace.server.routers.chat.ChatRouter
import br.meetingplace.server.routers.community.CommunityRouter
import br.meetingplace.server.routers.groups.GroupRouter
import br.meetingplace.server.routers.search.SearchRouter
import br.meetingplace.server.routers.topics.TopicRouter
import br.meetingplace.server.routers.user.userRouter
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    val application = embeddedServer(Netty, port) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }
            TopicRouter()
            SearchRouter()
            userRouter()
            CommunityRouter()
            GroupRouter()
            ChatRouter()
        }
    }.start(wait = true)
}


