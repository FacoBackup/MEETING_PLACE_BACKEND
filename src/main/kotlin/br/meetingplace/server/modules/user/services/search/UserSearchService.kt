package br.meetingplace.server.modules.user.services.search

import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.response.UserSearchDTO

object UserSearchService {

    suspend fun searchUser(requester: Long, maxID: Long?,userName: String, userDAO: UI, userSocialDAO: SI): List<UserSearchDTO>{
        return try{
            val users = if(maxID != null) userDAO.searchUserByMaxID(userName, requester, maxID) else userDAO.searchUser(userName, requester)
            val result = mutableListOf<UserSearchDTO>()

            for(i in users.indices){
                val isFollowing = userSocialDAO.check(followedID = users[i].userID, userID = requester)
                result.add(UserSearchDTO(name = users[i].name,email = users[i].email,imageURL = users[i].imageURL,isFollowing = isFollowing, userID = users[i].userID))
            }

            result
        }catch (e: Exception){
            listOf()
        }
    }
}