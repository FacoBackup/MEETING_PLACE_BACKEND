package br.meetingplace.server.modules.user.services.profile

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.dto.requests.RequestProfileUpdate
import org.postgresql.util.PSQLException

object UserUpdateService {

    fun updateProfile(data: RequestProfileUpdate, userDAO: UI) : Status {
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