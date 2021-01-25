package br.meetingplace.server.modules.user.services.factory

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import io.ktor.http.*
import org.postgresql.util.PSQLException


object UserFactoryService{

    suspend fun create(data: RequestUserCreation, userDAO: UI): HttpStatusCode {
        return try{
            if(userDAO.readAllByAttribute(email = data.email, data.userName, null, null, phoneNumber = data.phoneNumber, null, null).isNullOrEmpty()  && data.email.isNotBlank() && data.name.isNotBlank() && data.userName.isNotBlank() &&
                data.phoneNumber.isNotBlank() &&
                data.password.length >= 8&&
                data.email.length > 11 &&
                data.email.contains("@"))

                userDAO.create(data)
            else
                HttpStatusCode.ExpectationFailed
        }catch (e: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
}