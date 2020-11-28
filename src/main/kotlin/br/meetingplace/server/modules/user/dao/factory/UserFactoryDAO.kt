package br.meetingplace.server.modules.user.dao.factory

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.users.data.UserCreationData
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormat.forPattern
import org.postgresql.util.PSQLException
import java.time.format.DateTimeFormatter
import java.util.*


object UserFactoryDAO{

    fun create(data: UserCreationData): Status {
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