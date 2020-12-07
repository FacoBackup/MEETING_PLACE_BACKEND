package br.meetingplace.server.modules.user.services.social

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.requests.RequestSocial
import io.ktor.http.*

object UserSocialService {

    fun follow(requester: String,data: RequestSocial, userSocialDAO:SI, communityMemberDAO: CMI, communityDAO: CI, userDAO: UI): HttpStatusCode {
        return try {
            when(data.community){
                true-> {
                    if(communityDAO.read(data.subjectID) != null && userDAO.check(requester))
                        communityMemberDAO.create(requester, communityID = data.subjectID, MemberType.FOLLOWER.toString())
                    else HttpStatusCode.FailedDependency
                }
                false-> {
                    if(userDAO.read(data.subjectID) != null && userDAO.check(requester))
                        userSocialDAO.create(userID = requester, followedID = data.subjectID)
                    else HttpStatusCode.FailedDependency
                }
            }
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun unfollow(requester: String,data: RequestSocial, userSocialDAO:SI, communityMemberDAO: CMI): HttpStatusCode {
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