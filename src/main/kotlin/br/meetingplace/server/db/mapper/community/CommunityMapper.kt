package br.meetingplace.server.db.mapper.community

import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.modules.community.dto.CommunityDTO
import br.meetingplace.server.modules.community.dto.CommunityMembersDTO
import org.jetbrains.exposed.sql.ResultRow

object CommunityMapper: CommunityMapperInterface {
    override fun mapCommunityDTO(it: ResultRow): CommunityDTO {
        return CommunityDTO(name = it[Community.name], id = it[Community.id],about = it[Community.imageURL], imageURL =  it[Community.imageURL], creationDate = it[Community.creationDate].toString("dd-MM-yyyy"))
    }

    override fun mapCommunityMembersDTO(it: ResultRow): CommunityMembersDTO {
        return CommunityMembersDTO(communityID = it[CommunityMember.communityID], admin = it[CommunityMember.admin], userID = it[CommunityMember.userID])
    }
}