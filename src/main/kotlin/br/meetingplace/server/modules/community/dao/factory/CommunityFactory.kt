package br.meetingplace.server.modules.community.dao.factory

import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.RequestCreationData
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object CommunityFactory {

    fun create(data: RequestCreationData): Status {
        return try {
            if(transaction { !User.select { User.id eq data.userID }.empty() }){
                transaction {
                    Community.insert {
                        it[name] = data.name
                        it[id] = UUID.randomUUID().toString()
                        it[imageURL] = data.imageURL
                        it[creationDate] =  DateTime.now()
                        it[about] = data.about
                    }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}