package br.meetingplace.server.modules.user.dao.profile

import br.meetingplace.server.db.mapper.user.UserMapperInterface
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.users.data.ProfileData
import org.jetbrains.exposed.sql.select

object ProfileDAO {

    fun updateProfile(data: ProfileData, userMapper: UserMapperInterface) : Status {
        return try {
            val user =User.select{ User.id eq data.userID }.map { userMapper.mapUser(it)}.firstOrNull()
            if (user != null) {
                when{
                    !data.about.isNullOrBlank() && data.imageURL.isNullOrBlank()-> user.about = data.about
                    data.about.isNullOrBlank() && !data.imageURL.isNullOrBlank()-> user.imageURL = data.imageURL
                }
                Status(statusCode = 200, StatusMessages.OK)
            }else Status(statusCode = 404, StatusMessages.NOT_FOUND)
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}