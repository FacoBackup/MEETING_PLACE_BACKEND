package br.meetingplace.server.modules.community.services.read

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.response.RelatedCommunitiesDTO

object CommunityReadService {
    suspend fun readAllRelatedCommunities(requester: String, communityDAO: CI, communityMemberDAO: CMI): List<RelatedCommunitiesDTO>{
        return try {
            val communities = mutableListOf<RelatedCommunitiesDTO>()
            val relatedCommunities = communityMemberDAO.readByUser(requester)
            for(i in relatedCommunities.indices){
                val community = communityDAO.read(relatedCommunities[i].communityID)
                if(community != null){
                    communities.add(RelatedCommunitiesDTO(
                        name = community.name,
                        about = community.about,
                        parentCommunityName = community.parentCommunityID?.let { communityDAO.read(id = it) }?.name,
                        role = relatedCommunities[i].role
                    ))
                }

            }
            communities
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readCommunityByName(requester: String, name: String, communityDAO: CI, communityMemberDAO: CMI): List<RelatedCommunitiesDTO>{
        return try {
            val communities = communityDAO.readByName(name)
            val response = mutableListOf<RelatedCommunitiesDTO>()
            for(i in communities.indices){
                val communityMember = communityMemberDAO.read(communityID = communities[i].id, userID = requester)
                response.add(RelatedCommunitiesDTO(
                    name = communities[i].name,
                    about = communities[i].about,
                    parentCommunityName = communities[i].parentCommunityID?.let { communityDAO.read(id = it) }?.name,
                    role = communityMember?.role ?: ""
                ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
}