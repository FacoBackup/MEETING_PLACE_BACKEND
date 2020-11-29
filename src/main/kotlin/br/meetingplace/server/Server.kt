package br.meetingplace.server

import br.meetingplace.server.db.settings.dbSettings
import br.meetingplace.server.modules.message.db.Message
import br.meetingplace.server.modules.message.db.MessageOpinions
import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.modules.groups.db.Group
import br.meetingplace.server.modules.groups.db.GroupMember
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.topic.db.TopicOpinions
import br.meetingplace.server.modules.user.db.Social
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.routers.chat.messageRouter
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
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {

    val db = dbSettings(host = "localhost", dbName = "apidb", user = "api", password = "12345" )
    try {
        transaction {
            SchemaUtils.create(
                    User, Social,
                    Group, GroupMember,
                    Message, MessageOpinions,
                    Community, CommunityMember,
                    Topic, TopicOpinions)
        }
    }catch (e: Exception){
        println(e.message)
    }
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
                messageRouter()
            }
        }.start(wait = true)
    }
}


