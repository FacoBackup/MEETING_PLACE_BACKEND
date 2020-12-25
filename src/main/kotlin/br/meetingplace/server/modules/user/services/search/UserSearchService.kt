package br.meetingplace.server.modules.user.services.search

import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.response.UserSearchDTO

object UserSearchService {
    suspend fun searchUser(requester: String, userID: String, userDAO: UI, userSocialDAO: SI): List<UserSearchDTO>{
        return try{
            val user = userDAO.readByName(userID, requester)
            val result = mutableListOf<UserSearchDTO>()
            if(userDAO.check(requester)){
                for(i in user.indices){
                    val isFollowing = userSocialDAO.check(followedID = user[i].email, userID = requester)
                    result.add(UserSearchDTO(name = user[i].name,email = user[i].email,imageURL = user[i].imageURL,isFollowing = isFollowing))
                }
            }
            result
        }catch (e: Exception){
            listOf()
        }
    }
}