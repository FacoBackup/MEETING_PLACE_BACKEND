package br.meetingplace.server.modules.community.services.update

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityUpdate
import io.ktor.http.*

object CommunityUpdateService {
    fun updateCommunity(requester: String,data: RequestCommunityUpdate, community: CI, member: CMI): HttpStatusCode {
        return try {
            val memberData = member.read(communityID = data.communityID, userID = requester)

            if(memberData != null &&
              (memberData.role == MemberType.LEADER.toString() ||
               memberData.role == MemberType.DIRECTOR.toString() ||
               memberData.role == MemberType.MEMBER.toString())){

               community.update(communityID = data.communityID, name = data.name, about = data.about, parentID = data.parentCommunityID)
            }else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}