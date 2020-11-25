package br.meetingplace.server.modules.user.dao.factory

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.users.data.UserCreationData
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime
import java.util.*


object UserFactoryDAO{

    fun create(data: UserCreationData): Status {
        return try{
            User.insert { it[id] = UUID.randomUUID().toString()
                it[userName] = data.userName
                it[email] = data.email
                it[gender] = gender
                it[birth] = DateTime.parse(data.birthDate)
                it[imageURL] = null
                it[about] = null
                it[cityOfBirth] = data.cityOfBirth
                it[phoneNumber] = phoneNumber
            }
            Status(statusCode = 200, StatusMessages.OK)
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}