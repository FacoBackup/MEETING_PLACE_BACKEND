package br.meetingplace.server.modules.community.services.read

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.response.CommunityInfoDTO
import br.meetingplace.server.modules.community.dto.response.CommunityRelatedUsersDTO
import br.meetingplace.server.modules.community.dto.response.SimplifiedUserCommunityDTO
import br.meetingplace.server.modules.community.dto.response.UserCommunitiesDTO
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.user.dao.user.UI

object CommunityReadService {
    suspend fun readModeratorIn(requester: Long, communityDAO: CI, communityMemberDAO: CMI): List<UserCommunitiesDTO>{
        return try {
            val communities = mutableListOf<UserCommunitiesDTO>()
            val relatedCommunities = communityMemberDAO.readModeratorIn(requester)
            for(i in relatedCommunities.indices){
                val community = communityDAO.read(relatedCommunities[i].communityID)
                if(community != null){
                    communities.add(UserCommunitiesDTO(
                        name = community.name,
                        about = community.about,
                        relatedCommunityName = community.mainCommunity?.let { communityDAO.read(id = it) }?.name,
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
    suspend fun readAllRelatedCommunities(requester: Long, communityID: Long, communityDAO: CI, communityMemberDAO: CMI): List<SimplifiedUserCommunityDTO>{
        return try {
            val response = mutableListOf<SimplifiedUserCommunityDTO>()
            val relatedCommunities = communityDAO.readRelatedCommunities(communityID)

            for(i in relatedCommunities.indices){
                val communityMember = communityMemberDAO.read(communityID = relatedCommunities[i].id, userID = requester)
                val community = communityDAO.read(relatedCommunities[i].id)
                if(community != null)
                    response.add(SimplifiedUserCommunityDTO(
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

    suspend fun readCommunityByID(communityID: Long, requester: Long, communityDAO: CI,topicDAO: TI, communityMemberDAO: CMI): CommunityInfoDTO? {
        return try {
            val community = communityDAO.read(communityID)

            if(community != null){
                val mainCommunity = community.mainCommunity?.let { communityDAO.read(it) }
                val member = communityMemberDAO.read(communityID, requester)

                CommunityInfoDTO(
                    name = community.name,
                    about = community.about,
                    communityID =community.id,
                    creationDate =community.creationDate,
                    mainCommunityID = community.mainCommunity,
                    mainCommunityPic = mainCommunity?.imageURL,
                    mainCommunityName = mainCommunity?.name,
                    imageURL = community.imageURL,
                    backgroundImageURL = community.backgroundImageURL,
                    role = member?.role,
                    followers =  communityMemberDAO.readFollowersQuantity(community.id),
                    members =  communityMemberDAO.readMembersQuantity(community.id),
                    mods = communityMemberDAO.readModsQuantity(community.id),
                    topics = topicDAO.readTopicsQuantityByCommunity(community.id)
                )
            }
            else
                null
        }catch (e: Exception){
            null
        }
    }

    suspend fun readAllUserCommunities(requester: Long, communityDAO: CI, communityMemberDAO: CMI): List<UserCommunitiesDTO>{
        return try {
            val communities = mutableListOf<UserCommunitiesDTO>()
            val relatedCommunities = communityMemberDAO.readByUser(requester)
            for(i in relatedCommunities.indices){
                val community = communityDAO.read(relatedCommunities[i].communityID)
                if(community != null){
                    communities.add(UserCommunitiesDTO(
                        name = community.name,
                        about = community.about,
                        relatedCommunityName = community.mainCommunity?.let { communityDAO.read(id = it) }?.name,
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
    suspend fun readFollowers(communityID: Long, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
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
                        userEmail = user.email,
                        affiliatedCommunityID = communityID
                    ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readMods(communityID: Long, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
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
                        userEmail = user.email,
                        affiliatedCommunityID = communityID
                    ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readMembers(communityID: Long, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
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
                        userEmail = user.email,
                        affiliatedCommunityID = communityID
                    ))
            }

            response
        }catch (e: Exception){
            listOf()
        }
    }

    suspend fun readUsersRelatedToCommunity(communityID: Long,communityDAO: CI, communityMemberDAO: CMI, userDAO: UI): List<CommunityRelatedUsersDTO>{
        return try {
            val membersMainCommunity = communityMemberDAO.readByCommunity(communityID)
            val response = mutableListOf<CommunityRelatedUsersDTO>()
            val parentCommunities = communityDAO.readRelatedCommunities(communityID)

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
                            userEmail = user.email,
                            affiliatedCommunityID = parentCommunities[j].id
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
                        userEmail = user.email,
                        affiliatedCommunityID = communityID
                    ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
}