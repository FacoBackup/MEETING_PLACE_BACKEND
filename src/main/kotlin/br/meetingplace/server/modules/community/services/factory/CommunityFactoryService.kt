package br.meetingplace.server.modules.community.services.factory

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object CommunityFactoryService {

    suspend fun create(requester: String,data: RequestCommunityCreation, communityDAO: CI, userDAO: UI): HttpStatusCode {
        return try {
            if(userDAO.read(requester) != null)
                communityDAO.create(data)
            else HttpStatusCode.NoContent
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}