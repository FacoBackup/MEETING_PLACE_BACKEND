package br.meetingplace.server.modules.user.service.social

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.request.dto.generic.SubjectDTO

object UserSocialService {

    fun follow(data: SubjectDTO,userSocialDAO:SI, communityMemberDAO: CMI, communityDAO: CI, userDAO: UI): Status {
        return try {
            when(data.community){
                true-> {
                    if(communityDAO.read(data.subjectID) != null)
                        communityMemberDAO.create(data.userID, communityID = data.subjectID, MemberType.FOLLOWER.toString())
                    else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
                false-> {
                    if(userDAO.read(data.subjectID) != null)
                        userSocialDAO.create(userID = data.userID, followedID = data.subjectID)
                    else Status(statusCode = 404, StatusMessages.NOT_FOUND)
                }
            }
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun unfollow(data: SubjectDTO, userSocialDAO:SI, communityMemberDAO: CMI): Status {
        return try {
            when(data.community){
                true-> communityMemberDAO.delete(data.subjectID, userID = data.userID)
                false-> userSocialDAO.delete(userID = data.userID, data.subjectID)
            }
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}