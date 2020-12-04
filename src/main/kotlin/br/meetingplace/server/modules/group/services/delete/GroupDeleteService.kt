package br.meetingplace.server.modules.group.services.delete

import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.modules.group.dto.requests.RequestGroup

object GroupDeleteService{
    fun delete(data: RequestGroup, groupMemberDAO: GMI, groupDAO: GI): Status {
        return try{
            val member = groupMemberDAO.read(userID = data.userID, groupID = data.groupID)
            if(member != null && member.admin)
                groupDAO.delete(data.groupID)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}