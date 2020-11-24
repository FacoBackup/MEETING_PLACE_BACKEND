package br.meetingplace.server

import br.meetingplace.server.db.settings.Settings
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
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val dbSettings = Settings
    val port = System.getenv("PORT")?.toInt() ?: 8080
    transaction {
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


