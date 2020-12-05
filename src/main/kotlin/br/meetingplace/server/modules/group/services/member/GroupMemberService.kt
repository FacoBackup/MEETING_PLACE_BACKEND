package br.meetingplace.server.modules.group.services.member

import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.modules.group.dto.requests.RequestGroupMember
import io.ktor.http.*
import org.postgresql.util.PSQLException

object GroupMemberService {
    fun addMember(data: RequestGroupMember, groupMemberDAO: GMI): HttpStatusCode {
        return try{
            if(groupMemberDAO.check(userID = data.userID, groupID = data.groupID) == HttpStatusCode.Found &&
               groupMemberDAO.check(userID = data.memberID, groupID = data.groupID) == HttpStatusCode.NotFound)

               groupMemberDAO.create(data.memberID, groupID = data.groupID, false)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun removeMember(data: RequestGroupMember, groupMemberDAO: GMI): HttpStatusCode {
        return try {
            val userMember = groupMemberDAO.read(data.userID, groupID = data.groupID)
            if(userMember != null && userMember.admin)
                groupMemberDAO.delete(data.memberID, groupID = data.groupID)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
    fun promoteMember(data: RequestGroupMember, memberDAO: GMI): HttpStatusCode{
        return try {
            val member = memberDAO.read(data.userID, groupID = data.groupID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.groupID, admin = true)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }

    }
    fun lowerMember(data: RequestGroupMember, memberDAO: GMI): HttpStatusCode{
        return try {
            val member = memberDAO.read(data.userID, groupID = data.groupID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.groupID, admin = false)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}