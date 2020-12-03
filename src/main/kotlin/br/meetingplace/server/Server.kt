package br.meetingplace.server

import br.meetingplace.server.db.settings.dbSettings
import br.meetingplace.server.modules.message.entities.Message
import br.meetingplace.server.modules.message.entities.MessageOpinion
import br.meetingplace.server.modules.community.entities.Community
import br.meetingplace.server.modules.community.entities.CommunityMember
import br.meetingplace.server.modules.group.entities.Group
import br.meetingplace.server.modules.group.entities.GroupMember
import br.meetingplace.server.modules.topic.entities.Topic
import br.meetingplace.server.modules.topic.entities.TopicOpinion
import br.meetingplace.server.modules.user.entities.Social
import br.meetingplace.server.modules.user.entities.User
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
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

fun main() {

    val db = dbSettings(host = "localhost", dbName = "apidb", user = "api", password = "12345" )
    try {
        transaction {
            SchemaUtils.create(
                    User, Social,
                    Group, GroupMember,
                    Message, MessageOpinion,
                    Community, CommunityMember,
                    Topic, TopicOpinion)
        }
    }catch (e: Exception){
        println(e.message)
    }catch (psql: PSQLException){
        println(psql.message)
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
                userRouter()
                communityRouter()
                groupRouter()
                messageRouter()
            }
        }.start(wait = true)
    }
}


