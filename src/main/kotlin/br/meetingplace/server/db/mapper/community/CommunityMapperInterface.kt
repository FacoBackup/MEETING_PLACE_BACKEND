package br.meetingplace.server.db.mapper.community

import br.meetingplace.server.modules.communityTODOTRANSACTIONS.dto.CommunityDTO
import br.meetingplace.server.modules.communityTODOTRANSACTIONS.dto.CommunityMembersDTO
import org.jetbrains.exposed.sql.ResultRow

interface CommunityMapperInterface {
    fun mapCommunityMembersDTO(it: ResultRow): CommunityMembersDTO
    fun mapCommunityDTO(it: ResultRow): CommunityDTO
}