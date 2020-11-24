package br.meetingplace.server.db.mapper.community

import br.meetingplace.server.modules.community.db.CommunityMembers
import br.meetingplace.server.modules.community.dto.CommunityDTO
import br.meetingplace.server.modules.community.dto.CommunityMembersDTO
import br.meetingplace.server.modules.user.dto.UserDTO
import org.jetbrains.exposed.sql.ResultRow

interface CommunityMapperInterface {
    fun mapCommunityMembersDTO(it: ResultRow): CommunityMembersDTO
    fun mapCommunityDTO(it: ResultRow): CommunityDTO
}