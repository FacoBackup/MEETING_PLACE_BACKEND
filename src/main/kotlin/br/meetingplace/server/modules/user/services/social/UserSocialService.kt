package br.meetingplace.server.modules.user.services.social

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestSocial
import io.ktor.http.*

object UserSocialService {

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