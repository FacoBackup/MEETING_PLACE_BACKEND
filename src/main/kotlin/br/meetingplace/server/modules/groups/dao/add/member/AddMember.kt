package br.meetingplace.server.modules.groups.dao.add.member

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.groups.db.GroupMember
import br.meetingplace.server.requests.generic.MemberOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object AddMember {
    fun addMember(data: MemberOperator): Status {
        return try{
            if(transaction { GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)} }.firstOrNull() != null)
                transaction {
                    GroupMember.insert {
                        it[groupID] = data.subjectID
                        it[userID] = data.memberEmail
                        it[admin] = false
                    }
                }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}