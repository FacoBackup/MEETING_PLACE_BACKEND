package br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dao.remove.member

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.db.GroupMember
import br.meetingplace.server.requests.generic.operators.MemberOperator
import org.jetbrains.exposed.sql.*

object RemoveMember {
    fun removeMember(data: MemberOperator): Status {
        return try {
            if(!GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty())
                GroupMember.deleteWhere { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.memberEmail)}
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}