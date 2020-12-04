package br.meetingplace.server.modules.community.services.approval

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.community.dto.requests.RequestApproval
import io.ktor.http.*

object CommunityApprovalService {
    fun approveGroup(data: RequestApproval, memberDAO: CMI, groupDAO: GI): HttpStatusCode {
        return try{
            val group = groupDAO.read(groupID = data.serviceID)
            val memberData = memberDAO.read(data.communityID, userID = data.userID)

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
    fun disapproveGroup(data: RequestApproval, memberDAO: CMI, groupDAO: GI): HttpStatusCode{
        return try{
            val group = groupDAO.read(groupID = data.serviceID)
            val memberData = memberDAO.read(data.communityID, userID = data.userID)

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