package br.meetingplace.server.modules.groupTODO.service.delete

import br.meetingplace.server.modules.groupTODO.dao.GI
import br.meetingplace.server.modules.groupTODO.dao.member.GMI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.request.dto.generic.SubjectDTO
import org.postgresql.util.PSQLException

object GroupDeleteService{
    fun delete(data: SubjectDTO, memberDAO: GMI, groupDAO: GI): Status {
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