package br.meetingplace.server.modules.group.dao.member

import br.meetingplace.server.modules.group.dto.GroupMemberDTO
import br.meetingplace.server.response.status.Status

interface GMI {
    fun create(userID: String, groupID: String, admin: Boolean): Status
    fun read(userID: String, groupID: String): GroupMemberDTO?
    fun update(userID: String, groupID: String, admin: Boolean): Status
    fun delete(userID: String, groupID: String): Status
}