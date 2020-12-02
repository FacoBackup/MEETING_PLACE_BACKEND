package br.meetingplace.server.modules.groupTODO.service.member

import br.meetingplace.server.modules.groupTODO.dao.GI
import br.meetingplace.server.modules.groupTODO.dao.member.GMI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.request.dto.generic.MemberDTO
import org.postgresql.util.PSQLException

object GroupMemberService {
    fun addMember(data: MemberDTO, memberDAO: GMI, groupDAO: GI): Status {
        return try{
            if(memberDAO.read(userID = data.userID, groupID = data.subjectID) != null &&
               memberDAO.read(userID = data.memberID, groupID = data.subjectID) == null)

               memberDAO.create(data.memberID, groupID = data.subjectID, false)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException) {
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun removeMember(data: MemberDTO, memberDAO: GMI): Status {
        return try {
            val member = memberDAO.read(data.userID, groupID = data.subjectID)
            if(member != null && member.admin)
                memberDAO.delete(data.memberID, groupID = data.subjectID)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    fun promoteMember(data: MemberDTO, memberDAO: GMI): Status{
        return try {
            val member = memberDAO.read(data.userID, groupID = data.subjectID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.subjectID, admin = true)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }

    }
    fun lowerMember(data: MemberDTO, memberDAO: GMI): Status{
        return try {
            val member = memberDAO.read(data.userID, groupID = data.subjectID)
            if(member != null && member.admin)
                memberDAO.update(data.memberID, groupID = data.subjectID, admin = false)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}