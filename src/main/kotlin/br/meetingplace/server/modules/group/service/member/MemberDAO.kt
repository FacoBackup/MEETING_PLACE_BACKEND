package br.meetingplace.server.modules.group.service.member

import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.group.entitie.GroupMember
import br.meetingplace.server.request.dto.generic.MemberDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object MemberDAO {
    fun addMember(data: MemberDTO): Status {
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

    fun removeMember(data: MemberDTO): Status {
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