package br.meetingplace.server.modules.group.services.delete

import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.group.dto.requests.RequestGroup
import org.postgresql.util.PSQLException

object GroupDeleteService{
    fun delete(data: RequestGroup, memberDAO: GMI, groupDAO: GI): Status {
        return try{
            val member = memberDAO.read(userID = data.userID, groupID = data.subjectID)
            if(member != null && member.admin)
                groupDAO.delete(data.subjectID)
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}