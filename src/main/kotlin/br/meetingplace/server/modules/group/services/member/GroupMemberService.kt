package br.meetingplace.server.modules.group.services.member

import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.group.dto.requests.RequestGroupMember
import org.postgresql.util.PSQLException

object GroupMemberService {
    fun addMember(data: RequestGroupMember, memberDAO: GMI, groupDAO: GI): Status {
        return try{
            if(memberDAO.read(userID = data.userID, groupID = data.groupID) != null &&
               memberDAO.read(userID = data.memberID, groupID = data.groupID) == null)

               memberDAO.create(data.memberID, groupID = data.groupID, false)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException) {
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun removeMember(data: RequestGroupMember, memberDAO: GMI): Status {
        return try {
            val member = memberDAO.read(data.userID, groupID = data.groupID)
            if(member != null && member.admin)
                memberDAO.delete(data.memberID, groupID = data.groupID)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    fun promoteMember(data: RequestGroupMember, memberDAO: GMI): Status{
        return try {
            val member = memberDAO.read(data.userID, groupID = data.groupID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.groupID, admin = true)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }

    }
    fun lowerMember(data: RequestGroupMember, memberDAO: GMI): Status{
        return try {
            val member = memberDAO.read(data.userID, groupID = data.groupID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.groupID, admin = false)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}