package br.meetingplace.server.modules.community.service.factory

import br.meetingplace.server.modules.community.entitie.Community
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.community.CommunityCreationDTO
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object CommunityFactoryDAO {

    fun create(data: CommunityCreationDTO): Status {
        return try {
            if(transaction { !User.select { User.id eq data.userID }.empty() }){
                transaction {
                    Community.insert {
                        it[name] = data.name
                        it[id] = UUID.randomUUID().toString()
                        it[imageURL] = data.imageURL
                        it[creationDate] =  DateTime.now()
                        it[about] = data.about
                        it[location] = data.location
                        it[parentCommunityID]= data.communityID
                    }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}