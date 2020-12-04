package br.meetingplace.server.modules.user.services.profile

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestProfileUpdate
import io.ktor.http.*
import org.postgresql.util.PSQLException

object UserUpdateService {

    fun updateProfile(data: RequestProfileUpdate, userDAO: UI) : HttpStatusCode {
        return try {
            userDAO.update(data.userID, about = data.about, imageURL = data.imageURL,
                           phoneNumber = data.phoneNumber, name = data.name,
                           nationality = data.nationality, city = data.city)
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
}