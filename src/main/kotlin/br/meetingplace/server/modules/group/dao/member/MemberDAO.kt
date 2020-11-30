package br.meetingplace.server.modules.group.dao.member

import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.group.db.GroupMember
import br.meetingplace.server.requests.generic.MemberOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MemberDAO {
    fun addMember(data: MemberOperator): Status {
        return try{
            if(transaction { !GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty() })
                transaction {
                    GroupMember.insert {
                        it[groupID] = data.subjectID
                        it[userID] = data.memberID
                        it[admin] = false
                    }
                }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException) {
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun removeMember(data: MemberOperator): Status {
        return try {
            if(transaction { !GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty() })
                transaction {
                    GroupMember.deleteWhere { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.memberID)}
                }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}