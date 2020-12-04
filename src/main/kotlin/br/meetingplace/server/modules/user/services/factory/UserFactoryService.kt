package br.meetingplace.server.modules.user.services.factory

import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import io.ktor.http.*
import org.postgresql.util.PSQLException


object UserFactoryService{

    fun create(data: RequestUserCreation, userDAO: UI): HttpStatusCode {
        return try{
            if(userDAO.readAllByAttribute(name = data.userName, null,
                phoneNumber = data.phoneNumber,null,null).isEmpty() &&
                userDAO.read(data.email) == null)
                userDAO.create(data)
            else
                HttpStatusCode.InternalServerError
        }catch (e: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
}