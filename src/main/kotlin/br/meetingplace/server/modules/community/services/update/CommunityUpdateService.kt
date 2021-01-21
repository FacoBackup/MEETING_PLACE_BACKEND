package br.meetingplace.server.modules.community.services.update

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityUpdate
import io.ktor.http.*

object CommunityUpdateService {
    suspend  fun updateCommunity(requester: Long,data: RequestCommunityUpdate, community: CI, member: CMI): HttpStatusCode {
        return try {
            val memberData = member.read(communityID = data.communityID, userID = requester)

            if(memberData != null &&
              (memberData.role == MemberType.MODERATOR.toString() ||
               memberData.role == MemberType.MEMBER.toString())){

               community.update(
                   communityID = data.communityID,
                   about = data.about,
                   imageURL = data.imageURL,
                   backgroundImageURL = data.backgroundImageURL
                   )
            }else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}