package br.meetingplace.server.modules.group.dao.delete

import br.meetingplace.server.db.mapper.group.GroupMapperInterface
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.group.db.Group
import br.meetingplace.server.modules.group.db.GroupMember
import br.meetingplace.server.requests.generic.SimpleOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object GroupDeleteDAO{
    fun delete(data: SimpleOperator, groupMapper: GroupMapperInterface): Status {
        return try{
            val member = transaction { GroupMember.select { (GroupMember.userID eq data.userID) and (GroupMember.groupID eq data.subjectID)}.firstOrNull() }
            if(member!= null && groupMapper.mapGroupMembers(member).admin)
                transaction {
                    Group.deleteWhere { Group.id eq data.subjectID }
                }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}