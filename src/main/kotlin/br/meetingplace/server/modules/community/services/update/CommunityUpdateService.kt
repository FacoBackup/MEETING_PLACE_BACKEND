package br.meetingplace.server.modules.community.services.update

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityUpdate
import io.ktor.http.*

object CommunityUpdateService {
    suspend  fun updateCommunity(requester: Long, data: RequestCommunityUpdate, communityDAO: CI, communityMemberDAO: CMI): HttpStatusCode {
        return try {
            val memberData = communityMemberDAO.read(communityID = data.communityID, userID = requester)

            if(memberData != null &&
              (memberData.role == MemberType.MODERATOR.toString() ||
               memberData.role == MemberType.MEMBER.toString())){

               communityDAO.update(
                   communityID = data.communityID,
                   about = data.about,
                   imageURL = data.imageURL,
                   backgroundImageURL = data.backgroundImageURL,
                   name = data.name
                   )
            }else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}