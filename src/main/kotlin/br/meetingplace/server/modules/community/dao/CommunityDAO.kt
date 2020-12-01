package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.entitie.Community
import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.modules.community.dto.CommunityDTO
import br.meetingplace.server.modules.community.dto.CommunityMembersDTO
import org.jetbrains.exposed.sql.ResultRow

object CommunityDAO: CommunityDAOInterface {
    override fun mapCommunityDTO(it: ResultRow): CommunityDTO {
        return CommunityDTO(name = it[Community.name], id = it[Community.id],
                about = it[Community.imageURL], imageURL =  it[Community.imageURL],
                creationDate = it[Community.creationDate].toString(), location = it[Community.location],
                parentCommunityID = it[Community.parentCommunityID])
    }

    override fun mapCommunityMembersDTO(it: ResultRow): CommunityMembersDTO {
        return CommunityMembersDTO(communityID = it[CommunityMember.communityID], admin = it[CommunityMember.admin],
                userID = it[CommunityMember.userID])
    }
}