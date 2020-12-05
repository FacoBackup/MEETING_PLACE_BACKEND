package br.meetingplace.server.modules.group.services.delete

import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.modules.group.dto.requests.RequestGroup
import io.ktor.http.*

object GroupDeleteService{
    fun delete(data: RequestGroup, groupMemberDAO: GMI, groupDAO: GI): HttpStatusCode {
        return try{
            val member = groupMemberDAO.read(userID = data.userID, groupID = data.groupID)
            if(member != null && member.admin)
                groupDAO.delete(data.groupID)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}