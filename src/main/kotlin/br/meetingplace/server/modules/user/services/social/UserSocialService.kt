package br.meetingplace.server.modules.user.services.social

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestSocial
import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.dto.response.UserSocialDTO
import io.ktor.http.*

object UserSocialService {
    suspend fun readFollowers(requester: String, userSocialDAO: SI, userDAO: UI): List<UserSocialDTO>{
        return try {
            if(userDAO.check(requester)){
                val followers = userSocialDAO.readAll(userID = requester, following = false)
                val userFollowers = mutableListOf<UserSocialDTO>()
                for(i in followers.indices){
                    val follower = userDAO.readSocialByID(if(followers[i].followedID != requester) followers[i].followedID else followers[i].followerID )
                    if(follower != null)
                        userFollowers.add(follower)
                }
                userFollowers
            }
            else
                listOf()
        }catch (e: Exception){
            listOf()
        }

    }
    suspend fun readFollowing(requester: String, userSocialDAO: SI, userDAO: UI): List<UserSocialDTO>{
        return try {
            if(userDAO.check(requester)){
                val following = userSocialDAO.readAll(userID = requester, following = true)
                val userFollowing = mutableListOf<UserSocialDTO>()
                for(i in following.indices){
                    val followed = userDAO.readSocialByID(if(following[i].followerID != requester) following[i].followerID else following[i].followedID )
                    if(followed != null)
                        userFollowing.add(followed)
                }
                userFollowing
            }
            else
                listOf()
        }catch (e: Exception){
            listOf()
        }

    }
    suspend fun follow(requester: String,data: RequestSocial, userSocialDAO:SI, communityMemberDAO: CMI, communityDAO: CI, userDAO: UI): HttpStatusCode {
        return try {
            when(data.community){
                true-> {
                    if(communityDAO.check(data.subjectID) && userDAO.check(requester))
                        communityMemberDAO.create(requester, communityID = data.subjectID, MemberType.FOLLOWER.toString())
                    else HttpStatusCode.FailedDependency
                }
                false-> {
                    if(userDAO.check(data.subjectID) && userDAO.check(requester) && !userSocialDAO.check(userID = requester, followedID = data.subjectID))
                        userSocialDAO.create(userID = requester, followedID = data.subjectID)
                    else HttpStatusCode.FailedDependency
                }
            }
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    suspend fun unfollow(requester: String,data: RequestSocial, userSocialDAO:SI, communityMemberDAO: CMI): HttpStatusCode {
        return try {
            when(data.community){
                true-> communityMemberDAO.delete(data.subjectID, userID = requester)
                false-> userSocialDAO.delete(userID = requester, data.subjectID)
            }
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}