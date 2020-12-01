package br.meetingplace.server.modules.group.service.delete

import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.group.entitie.Group
import br.meetingplace.server.modules.group.entitie.GroupMember
import br.meetingplace.server.request.dto.generic.SubjectDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object GroupDelete{
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