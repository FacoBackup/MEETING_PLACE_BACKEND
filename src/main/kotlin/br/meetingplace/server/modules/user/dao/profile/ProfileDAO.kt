package br.meetingplace.server.modules.user.dao.profile

import br.meetingplace.server.db.mapper.user.UserMapperInterface
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.users.data.ProfileData
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object ProfileDAO {

    fun updateProfile(data: ProfileData) : Status {
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
        }
    }
}