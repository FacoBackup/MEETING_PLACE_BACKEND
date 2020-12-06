package br.meetingplace.server.modules.community.services.factory

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object CommunityFactoryService {

    fun create(data: RequestCommunityCreation, communityDAO: CI, userDAO: UI): HttpStatusCode {
        return try {
            if(userDAO.read(data.userID) != null)
                communityDAO.create(data)
            else HttpStatusCode.NoContent
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}