package br.meetingplace.server.db.mapper.group

import br.meetingplace.server.modules.groups.dto.GroupDTO
import br.meetingplace.server.modules.groups.dto.GroupMembersDTO
import br.meetingplace.server.modules.user.dto.UserDTO
import org.jetbrains.exposed.sql.ResultRow

interface GroupMapperInterface {
    fun mapGroupMembers(it: ResultRow): GroupMembersDTO
    fun mapGroup(it: ResultRow): GroupDTO
}