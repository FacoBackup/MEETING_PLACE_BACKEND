package br.meetingplace.server.modules.group.dao

import br.meetingplace.server.modules.group.dto.response.GroupDTO
import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation
import br.meetingplace.server.response.status.Status

interface GI {
    fun create(data: RequestGroupCreation, approved: Boolean): Status
    fun read(groupID: String): GroupDTO?
    fun update(groupID: String, name: String?, about: String?, approved: Boolean): Status
    fun delete(groupID: String): Status
}