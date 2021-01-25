package br.meetingplace.server.modules.search.service

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.response.SimplifiedUserCommunityDTO
import br.meetingplace.server.modules.community.dto.response.UserCommunitiesDTO
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.response.UserSearchDTO

object SearchService {

    suspend fun searchUser(requester: Long, maxID: Long?,userName: String, userDAO: UI, userSocialDAO: SI): List<UserSearchDTO>{
        return try{
            val users = if(maxID != null) userDAO.searchUserByMaxID(userName, requester, maxID) else userDAO.searchUser(userName, requester)
            val result = mutableListOf<UserSearchDTO>()

            for(i in users.indices){
                val isFollowing = userSocialDAO.check(followedID = users[i].userID, userID = requester)
                result.add(UserSearchDTO(
                    name = users[i].name,
                    email = users[i].email,
                    imageURL = users[i].imageURL,
                    isFollowing = isFollowing,
                    userID = users[i].userID,
                    userName = users[i].userName
                    ))
            }

            result
        }catch (e: Exception){
            listOf()
        }
    }

    suspend fun searchCommunity(requester: Long, name: String,maxID: Long?, communityDAO: CI, communityMemberDAO: CMI): List<SimplifiedUserCommunityDTO>{
        return try {
            val communities = if(maxID == null) communityDAO.searchByNewest(name) else communityDAO.searchByMaxID(name, maxID)
            val response = mutableListOf<SimplifiedUserCommunityDTO>()
            for(i in communities.indices){
                val communityMember = communityMemberDAO.read(communityID = communities[i].communityID, userID = requester)
                response.add(
                    SimplifiedUserCommunityDTO(
                        name = communities[i].name,
                        about = communities[i].about,
                        role = communityMember?.role ?: "",
                        communityID = communities[i].communityID,
                        imageURL = communities[i].imageURL
                    )
                )
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
}