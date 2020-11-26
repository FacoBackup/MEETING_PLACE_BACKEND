package br.meetingplace.server.db.mapper.group

import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dto.GroupDTO
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dto.GroupMembersDTO
import org.jetbrains.exposed.sql.ResultRow

interface GroupMapperInterface {
    fun mapGroupMembers(it: ResultRow): GroupMembersDTO
    fun mapGroup(it: ResultRow): GroupDTO
}