package br.meetingplace.server.modules.group.services.member

import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.modules.group.dto.requests.RequestGroupMember
import io.ktor.http.*

object GroupMemberService {
    fun addMember(requester: String,data: RequestGroupMember, groupMemberDAO: GMI): HttpStatusCode {
        return try{
            if(groupMemberDAO.check(userID = requester, groupID = data.groupID) == HttpStatusCode.Found &&
               groupMemberDAO.check(userID = data.memberID, groupID = data.groupID) == HttpStatusCode.NotFound)

               groupMemberDAO.create(data.memberID, groupID = data.groupID, false)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun removeMember(requester: String,data: RequestGroupMember, groupMemberDAO: GMI): HttpStatusCode {
        return try {
            val userMember = groupMemberDAO.read(requester, groupID = data.groupID)
            if(userMember != null && userMember.admin)
                groupMemberDAO.delete(data.memberID, groupID = data.groupID)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
    fun promoteMember(requester: String,data: RequestGroupMember, memberDAO: GMI): HttpStatusCode{
        return try {
            val member = memberDAO.read(requester, groupID = data.groupID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.groupID, admin = true)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }

    }
    fun lowerMember(requester: String,data: RequestGroupMember, memberDAO: GMI): HttpStatusCode{
        return try {
            val member = memberDAO.read(requester, groupID = data.groupID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.groupID, admin = false)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}