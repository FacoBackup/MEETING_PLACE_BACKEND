package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation

interface CI {
    fun create(data: RequestCommunityCreation):Status
    fun read(id: String): CommunityDTO?
    fun update(communityID: String, name: String?, about: String?, parentID: String?):Status
    fun delete(id: String):Status
}