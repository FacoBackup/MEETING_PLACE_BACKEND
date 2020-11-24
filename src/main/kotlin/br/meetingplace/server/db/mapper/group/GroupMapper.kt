package br.meetingplace.server.db.mapper.group

import br.meetingplace.server.modules.groups.dto.GroupDTO
import br.meetingplace.server.modules.groups.dto.GroupMembersDTO
import org.jetbrains.exposed.sql.ResultRow

class GroupMapper: GroupMapperInterface {
    override fun mapGroup(it: ResultRow): GroupDTO {
        TODO("Not yet implemented")
    }

    override fun mapGroupMembers(it: ResultRow): GroupMembersDTO {
        TODO("Not yet implemented")
    }
}