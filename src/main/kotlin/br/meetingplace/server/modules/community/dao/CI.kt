package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.CommunityDTO
import br.meetingplace.server.request.dto.community.CommunityCreationDTO
import br.meetingplace.server.response.status.Status

interface CI {
    fun create(data: CommunityCreationDTO):Status
    fun read(id: String): CommunityDTO?
    fun update(communityID: String, name: String?, about: String?, parentID: String?):Status
    fun delete(id: String):Status
}