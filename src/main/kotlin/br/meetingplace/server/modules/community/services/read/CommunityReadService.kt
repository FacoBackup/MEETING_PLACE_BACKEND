package br.meetingplace.server.modules.community.services.read

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.dto.response.CommunityInfoDTO
import br.meetingplace.server.modules.community.dto.response.CommunityRelatedUsersDTO
import br.meetingplace.server.modules.community.dto.response.RelatedCommunitiesDTO
import br.meetingplace.server.modules.user.dao.user.UI

object CommunityReadService {
    suspend fun readCommunityByID(communityID: String, requester: String, communityDAO: CI, communityMemberDAO: CMI): CommunityInfoDTO? {
        return try {
            val community = communityDAO.read(communityID)

            if(community != null){
                val parentCommunity = community.parentCommunityID?.let { communityDAO.read(it) }
                val member = communityMemberDAO.read(communityID, requester)

                CommunityInfoDTO(
                    name = community.name,
                    about = community.about,
                    communityID =community.id,
                    creationDate =community.creationDate,
                    parentCommunityID = community.parentCommunityID,
                    parentCommunityImageURL = parentCommunity?.imageURL,
                    parentCommunityName = parentCommunity?.name,
                    imageURL = community.imageURL,
                    backgroundImageURL = community.backgroundImageURL,
                    role = member?.role
                )
            }
            else
                null
        }catch (e: Exception){
            null
        }
    }
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
                        role = relatedCommunities[i].role,
                        communityID = community.id,
                        imageURL = community.imageURL
                    ))
                }

            }
            communities
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readUsersRelatedToCommunity(communityID: String, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
        return try {
            val members = communityMemberDAO.readByCommunity(communityID)
            val response = mutableListOf<CommunityRelatedUsersDTO>()
            for(i in members.indices){
                val communityMember = communityMemberDAO.read(communityID = communityID, userID = members[i].userID)
                val user = userDAO.readByID(members[i].userID)
                if(communityMember != null && user != null)
                    response.add(CommunityRelatedUsersDTO(
                        userName = user.name,
                        userID = members[i].userID,
                        communityID = communityID,
                        role = communityMember.role,
                        userImageURL = user.imageURL
                    ))
            }
            response
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
                    role = communityMember?.role ?: "",
                    communityID = communities[i].id,
                    imageURL = communities[i].imageURL

                ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
}