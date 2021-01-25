package br.meetingplace.server.modules.user.services.profile

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestProfileUpdate
import io.ktor.http.*
import org.postgresql.util.PSQLException

object UserUpdateService {

    suspend fun updateProfile(requester: Long,data: RequestProfileUpdate, userDAO: UI) : HttpStatusCode {
        return try {
            userDAO.update(
                requester,
                about = data.about,
                imageURL = data.imageURL,
                phoneNumber = data.phoneNumber,
                name = data.name,
                nationality = data.nationality,
                city = data.city,
                backgroundImageURL = data.backgroundImageURL,
                category = data.category
                )

        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}