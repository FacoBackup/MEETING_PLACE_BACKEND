package br.meetingplace.server.modules.user.service.profile

import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.request.dto.users.ProfileUpdateDTO
import org.postgresql.util.PSQLException

object UserUpdateService {

    fun updateProfile(data: ProfileUpdateDTO, userDAO: UI) : Status {
        return try {
            userDAO.update(data.userID, about = data.about, imageURL = data.imageURL,
                           phoneNumber = data.phoneNumber, name = data.name,
                           nationality = data.nationality, city = data.city)
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}