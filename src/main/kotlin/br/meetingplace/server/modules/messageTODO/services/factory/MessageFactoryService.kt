package br.meetingplace.server.modules.messageTODO.services.factory

import br.meetingplace.server.modules.messageTODO.entities.Message
import br.meetingplace.server.modules.group.entities.Group
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entities.User
import br.meetingplace.server.modules.messageTODO.dto.requests.RequestMessageCreation
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*


object MessageFactoryService {

    fun createMessage(data: RequestMessageCreation): Status {
        return try {
            if(transaction { !User.select { User.id eq data.userID }.empty() })
                transaction {
                    Message.insert {
                        it[content] = data.message
                        it[imageURL] = data.imageURL
                        it[id] = UUID.randomUUID().toString()
                        it[creationDate] = DateTime.now()
                        it[type] = 0
                        it[creatorID] = data.userID
                        if(data.isGroup && transaction { !Group.select { Group.id eq data.receiverID }.empty() })
                            it[groupReceiverID] = data.receiverID
                        else if(!data.isGroup && transaction { !User.select { User.id eq data.receiverID }.empty() })
                            it[userReceiverID] = data.receiverID
                    }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

}