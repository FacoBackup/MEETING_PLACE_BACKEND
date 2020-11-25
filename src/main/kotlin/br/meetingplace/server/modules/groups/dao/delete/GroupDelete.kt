package br.meetingplace.server.modules.groups.dao.delete

import br.meetingplace.server.db.mapper.group.GroupMapperInterface
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.groups.db.Group
import br.meetingplace.server.modules.groups.db.GroupMember
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.operators.SimpleOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

object GroupDelete{
    fun delete(data: SimpleOperator, groupMapper: GroupMapperInterface): Status {
        return try{
            val member = GroupMember.select { (GroupMember.userID eq data.userID) and (GroupMember.groupID eq data.subjectID)}.firstOrNull()
            if(!User.select{ User.id eq data.userID }.empty() && member!= null && groupMapper.mapGroupMembers(member).admin)
                Group.deleteWhere { Group.id eq data.subjectID }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}