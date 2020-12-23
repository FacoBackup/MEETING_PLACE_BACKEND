package br.meetingplace.server.settings.db

import br.meetingplace.server.modules.authentication.entities.AccessLog
import br.meetingplace.server.modules.community.entities.Community
import br.meetingplace.server.modules.community.entities.CommunityMember
import br.meetingplace.server.modules.conversation.entities.conversation.ConversationEntity
import br.meetingplace.server.modules.conversation.entities.conversation.ConversationMemberEntity
import br.meetingplace.server.modules.conversation.entities.conversation.ConversationOwnersEntity
import br.meetingplace.server.modules.conversation.entities.messages.MessageEntity
import br.meetingplace.server.modules.conversation.entities.messages.MessageOpinionEntity
import br.meetingplace.server.modules.conversation.entities.messages.MessageStatusEntity
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
                    ConversationEntity,
                    ConversationMemberEntity,
                    MessageEntity,
                    MessageOpinionEntity,
                    MessageStatusEntity,
                    Community,
                    CommunityMember,
                    Topic,
                    TopicOpinion,
                    ConversationOwnersEntity
                )
            }
        }catch (e: Exception){
            println(e.message)
        }catch (psql: PSQLException){
            println(psql.message)
        }
    }
}
