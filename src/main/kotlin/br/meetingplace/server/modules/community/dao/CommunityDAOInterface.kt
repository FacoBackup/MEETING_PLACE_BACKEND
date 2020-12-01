package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.CommunityDTO
import br.meetingplace.server.modules.community.dto.CommunityMembersDTO
import org.jetbrains.exposed.sql.ResultRow

interface CommunityDAOInterface {
    fun mapCommunityMembersDTO(it: ResultRow): CommunityMembersDTO
    fun mapCommunityDTO(it: ResultRow): CommunityDTO
}