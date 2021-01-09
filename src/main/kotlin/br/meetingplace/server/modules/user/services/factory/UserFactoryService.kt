package br.meetingplace.server.modules.user.services.factory

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import io.ktor.http.*
import org.postgresql.util.PSQLException


object UserFactoryService{

    suspend fun create(data: RequestUserCreation, userDAO: UI): HttpStatusCode {
        return try{
            if(userDAO.readAllByAttribute(null, null,
                phoneNumber = data.phoneNumber,null,null).isEmpty() &&
                 !userDAO.check(data.email) && data.email.isNotBlank() && data.email.length > 11 && data.email.contains("@"))
                userDAO.create(data)

            else
                HttpStatusCode.InternalServerError
        }catch (e: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
}