package br.meetingplace.server

import br.meetingplace.server.db.settings.dbSettings
import br.meetingplace.server.routers.chat.chatRouter
import br.meetingplace.server.routers.community.communityRouter
import br.meetingplace.server.routers.groups.groupRouter
import br.meetingplace.server.routers.search.searchRouter
import br.meetingplace.server.routers.topics.topicRouter
import br.meetingplace.server.routers.user.userRouter
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {

    val db = dbSettings(host = "192.168.1.222", dbName = "testeapp", user = "user_testeapp", password = "kU01ZUQXH0" )
    println(if(db == null) db else "abc")
    if(db != null){
        val port = System.getenv("PORT")?.toInt() ?: 8080
        embeddedServer(Netty, port) {
            routing {
                install(ContentNegotiation) {
                    gson {
                        setPrettyPrinting()
                    }
                }
                topicRouter()
                searchRouter()
                userRouter()
                communityRouter()
                groupRouter()
                chatRouter()
            }
        }.start(wait = true)
    }
}


