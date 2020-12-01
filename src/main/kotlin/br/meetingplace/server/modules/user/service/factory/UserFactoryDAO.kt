package br.meetingplace.server.modules.user.service.factory

import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.users.UserCreationDTO
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.format.DateTimeFormat
import org.postgresql.util.PSQLException
import java.util.*


object UserFactoryDAO{

    fun create(data: UserCreationDTO): Status {
        return try{
            if(transaction { User.select { User.userName eq data.userName }.empty() }){
                transaction {
                    User.insert {
                        it[id] = UUID.randomUUID().toString()
                        it[userName] = data.userName
                        it[email] = data.email
                        it[gender] = data.gender
                        it[nationality] = data.nationality
                        it[birth] = DateTimeFormat.forPattern("dd-MM-yyyy").parseDateTime(data.birthDate)
                        it[imageURL] = null
                        it[about] = null
                        it[cityOfBirth] = data.cityOfBirth
                        it[phoneNumber] = data.phoneNumber
                    }
                }
                Status(statusCode = 200, StatusMessages.OK)
            }
            else{
                Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }

        }catch (e: PSQLException){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}