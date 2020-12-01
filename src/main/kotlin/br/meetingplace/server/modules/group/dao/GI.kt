package br.meetingplace.server.modules.group.dao

import br.meetingplace.server.modules.group.dto.GroupDTO
import br.meetingplace.server.modules.group.dto.GroupMemberDTO
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import br.meetingplace.server.response.status.Status
import org.jetbrains.exposed.sql.ResultRow

interface GI {
    fun create(data: GroupCreationDTO, approved: Boolean): Status
    fun read(groupID: String): GroupDTO?
    fun update(groupID: String, name: String?, about: String?, approved: Boolean): Status
    fun delete(groupID: String): Status
}