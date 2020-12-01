package br.meetingplace.server.modules.user.service.profile

import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.users.ProfileUpdateDTO
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.postgresql.util.PSQLException

object ProfileDAO {

    fun updateProfile(data: ProfileUpdateDTO) : Status {
        return try {
            transaction {
                User.update({ User.id eq data.userID}){
                    if(!data.about.isNullOrBlank())
                        it[about] = data.about
                    if(!data.imageURL.isNullOrBlank())
                        it[imageURL] = data.imageURL
                }
            }
            Status(statusCode = 200, StatusMessages.OK)
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}