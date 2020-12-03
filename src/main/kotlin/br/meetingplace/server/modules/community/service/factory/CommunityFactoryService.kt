package br.meetingplace.server.modules.community.service.factory

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.request.dto.community.CommunityCreationDTO

object CommunityFactoryService {

    fun create(data: CommunityCreationDTO, communityDAO: CI, userDAO: UI): Status {
        return try {
            if(userDAO.read(data.userID) != null)
                communityDAO.create(data)
            else Status(404, StatusMessages.NOT_FOUND)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}