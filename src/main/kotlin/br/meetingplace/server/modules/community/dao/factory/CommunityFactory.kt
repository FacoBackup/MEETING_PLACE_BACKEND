package br.meetingplace.server.modules.community.dao.factory

import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.data.CreationData
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

object CommunityFactory {

    fun create(data: CreationData): Status {
        return try {
            if(!User.select { User.id eq data.userID }.empty()){
                Community.insert {
                    it[name] = data.name
                    it[id] = UUID.randomUUID().toString()
                    it[imageURL] = data.imageURL
                    it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                    it[about] = data.about
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}