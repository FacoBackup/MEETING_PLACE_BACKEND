package br.meetingplace.server.settings.db

import br.meetingplace.server.modules.authentication.entities.AccessLog
import br.meetingplace.server.modules.community.entities.Community
import br.meetingplace.server.modules.community.entities.CommunityMember
import br.meetingplace.server.modules.conversation.entities.Conversation
import br.meetingplace.server.modules.conversation.entities.ConversationMember
import br.meetingplace.server.modules.conversation.entities.Message
import br.meetingplace.server.modules.conversation.entities.MessageOpinion
import br.meetingplace.server.modules.topic.entities.Topic
import br.meetingplace.server.modules.topic.entities.TopicOpinion
import br.meetingplace.server.modules.user.entities.Social
import br.meetingplace.server.modules.user.entities.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException
import java.sql.SQLException
object DBSettings {
    fun dbSettings(host: String,dbName: String,user: String, password: String): Database?{
        return try{
            Database.connect(url = "jdbc:postgresql://$host/$dbName", driver = "org.postgresql.Driver", user = user, password = password)
        }catch(sql: SQLException ){
            null
        }catch (psql: PSQLException){
            null
        }catch (e: SQLException){
            null
        }
    }
    fun setUpTables(){
        try {
            transaction {
                SchemaUtils.create(
                    User,
                    Social,
                    AccessLog,
                    Conversation,
                    ConversationMember,
                    Message,
                    MessageOpinion,
                    Community,
                    CommunityMember,
                    Topic,
                    TopicOpinion
                )
            }
        }catch (e: Exception){
            println(e.message)
        }catch (psql: PSQLException){
            println(psql.message)
        }
    }
}
