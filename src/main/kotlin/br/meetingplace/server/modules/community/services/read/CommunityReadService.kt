package br.meetingplace.server.modules.community.services.read

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.response.CommunityInfoDTO
import br.meetingplace.server.modules.community.dto.response.CommunityRelatedUsersDTO
import br.meetingplace.server.modules.community.dto.response.SimplifiedCommunityDTO
import br.meetingplace.server.modules.community.dto.response.UserCommunitiesDTO
import br.meetingplace.server.modules.user.dao.user.UI

object CommunityReadService {
    suspend fun readAllRelatedCommunities(requester: String, communityID: String, communityDAO: CI, communityMemberDAO: CMI): List<SimplifiedCommunityDTO>{
        return try {
            val response = mutableListOf<SimplifiedCommunityDTO>()
            val relatedCommunities = communityDAO.readParentCommunities(communityID)

            for(i in relatedCommunities.indices){
                val communityMember = communityMemberDAO.read(communityID = relatedCommunities[i].id, userID = requester)
                val community = communityDAO.read(relatedCommunities[i].id)
                if(community != null)
                    response.add(SimplifiedCommunityDTO(
                        name = community.name,
                        about = community.about,
                        communityID = community.id,
                        role = communityMember?.role,
                        imageURL = community.imageURL
                    ))
            }

            response
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readCommunityByID(communityID: String, requester: String, communityDAO: CI, communityMemberDAO: CMI): CommunityInfoDTO? {
        return try {
            val community = communityDAO.read(communityID)

            if(community != null){
                val parentCommunity = community.relatedCommunityID?.let { communityDAO.read(it) }
                val member = communityMemberDAO.read(communityID, requester)

                CommunityInfoDTO(
                    name = community.name,
                    about = community.about,
                    communityID =community.id,
                    creationDate =community.creationDate,
                    relatedCommunityID = community.relatedCommunityID,
                    relatedCommunityImageURL = parentCommunity?.imageURL,
                    relatedCommunityName = parentCommunity?.name,
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
    suspend fun readAllUserCommunities(userID: String, communityDAO: CI, communityMemberDAO: CMI): List<UserCommunitiesDTO>{
        return try {
            val communities = mutableListOf<UserCommunitiesDTO>()
            val relatedCommunities = communityMemberDAO.readByUser(userID)
            for(i in relatedCommunities.indices){
                val community = communityDAO.read(relatedCommunities[i].communityID)
                if(community != null){
                    communities.add(UserCommunitiesDTO(
                        name = community.name,
                        about = community.about,
                        relatedCommunityName = community.relatedCommunityID?.let { communityDAO.read(id = it) }?.name,
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
    suspend fun readFollowers(communityID: String,communityDAO: CI, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
        return try {
            val followers = communityMemberDAO.readFollowers(communityID)
            val response = mutableListOf<CommunityRelatedUsersDTO>()
            for(i in followers.indices){
                val communityMember = communityMemberDAO.read(communityID = communityID, userID = followers[i].userID)
                val user = userDAO.readByID(followers[i].userID)
                if(communityMember != null && user != null)
                    response.add(CommunityRelatedUsersDTO(
                        userName = user.name,
                        userID = followers[i].userID,
                        communityID = communityID,
                        role = communityMember.role,
                        userImageURL = user.imageURL,
                        communityName = null,
                        userEmail = user.email
                    ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readMods(communityID: String,communityDAO: CI, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
        return try {
            val mods = communityMemberDAO.readMods(communityID)
            val response = mutableListOf<CommunityRelatedUsersDTO>()
            for(i in mods.indices){
                val communityMember = communityMemberDAO.read(communityID = communityID, userID = mods[i].userID)
                val user = userDAO.readByID(mods[i].userID)
                if(communityMember != null && user != null)
                    response.add(CommunityRelatedUsersDTO(
                        userName = user.name,
                        userID = mods[i].userID,
                        communityID = communityID,
                        role = communityMember.role,
                        userImageURL = user.imageURL,
                        communityName = null,
                        userEmail = user.email
                    ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readMembers(communityID: String,communityDAO: CI, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
        return try {
            val members = communityMemberDAO.readMembers(communityID)
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
                        userImageURL = user.imageURL,
                        communityName = null,
                        userEmail = user.email
                    ))
            }

            response
        }catch (e: Exception){
            listOf()
        }
    }

    suspend fun readUsersRelatedToCommunity(communityID: String,communityDAO: CI, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
        return try {
            val membersMainCommunity = communityMemberDAO.readByCommunity(communityID)
            val response = mutableListOf<CommunityRelatedUsersDTO>()
            val parentCommunities = communityDAO.readParentCommunities(communityID)

            for(j in parentCommunities.indices){
                val members = communityMemberDAO.readByCommunity(parentCommunities[j].id)
                for(i in members.indices){
                    val communityMember = communityMemberDAO.read(communityID = parentCommunities[j].id, userID = members[i].userID)
                    val user = userDAO.readByID(members[i].userID)
                    if(communityMember != null && user != null)
                        response.add(CommunityRelatedUsersDTO(
                            userName = user.name,
                            userID = members[i].userID,
                            communityID = parentCommunities[j].id,
                            role = communityMember.role,
                            userImageURL = user.imageURL,
                            communityName = parentCommunities[j].name,
                            userEmail = user.email
                        ))
                }
            }
            for(i in membersMainCommunity.indices){
                val communityMember = communityMemberDAO.read(communityID = communityID, userID = membersMainCommunity[i].userID)
                val user = userDAO.readByID(membersMainCommunity[i].userID)
                if(communityMember != null && user != null)
                    response.add(CommunityRelatedUsersDTO(
                        userName = user.name,
                        userID = membersMainCommunity[i].userID,
                        communityID = communityID,
                        role = communityMember.role,
                        userImageURL = user.imageURL,
                        communityName = null,
                        userEmail = user.email
                    ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readCommunityByName(requester: String, name: String, communityDAO: CI, communityMemberDAO: CMI): List<UserCommunitiesDTO>{
        return try {
            val communities = communityDAO.readByName(name)
            val response = mutableListOf<UserCommunitiesDTO>()
            for(i in communities.indices){
                val communityMember = communityMemberDAO.read(communityID = communities[i].id, userID = requester)
                response.add(UserCommunitiesDTO(
                    name = communities[i].name,
                    about = communities[i].about,
                    relatedCommunityName = communities[i].relatedCommunityID?.let { communityDAO.read(id = it) }?.name,
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