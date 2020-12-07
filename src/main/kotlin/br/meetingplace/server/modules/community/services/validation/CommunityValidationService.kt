package br.meetingplace.server.modules.community.services.validation

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestApproval
import br.meetingplace.server.modules.group.dao.GI
import io.ktor.http.*

object CommunityValidationService {
    fun approveGroup(requester: String,data: RequestApproval, memberDAO: CMI, groupDAO: GI): HttpStatusCode {
        return try{
            val group = groupDAO.read(groupID = data.serviceID)
            val memberData = memberDAO.read(data.communityID, userID = requester)

            if(memberData != null &&
              (memberData.role == MemberType.LEADER.toString() ||
               memberData.role == MemberType.DIRECTOR.toString()) &&
               group != null && !group.approved)
                groupDAO.update(groupID = data.serviceID, name = null, about = null, approved = true)
            else HttpStatusCode.InternalServerError

        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
    fun disapproveGroup(requester: String,data: RequestApproval, memberDAO: CMI, groupDAO: GI): HttpStatusCode{
        return try{
            val group = groupDAO.read(groupID = data.serviceID)
            val memberData = memberDAO.read(data.communityID, userID = requester)

            if(memberData != null &&
              (memberData.role == MemberType.LEADER.toString() ||
               memberData.role == MemberType.DIRECTOR.toString()) &&
                group != null && !group.approved)
                groupDAO.update(groupID = data.serviceID, name = null, about = null, approved = false)
            else HttpStatusCode.InternalServerError
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}