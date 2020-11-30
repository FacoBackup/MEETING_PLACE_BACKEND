package br.meetingplace.server.modules.member.dao

import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.group.db.GroupMember
import br.meetingplace.server.requests.generic.MemberOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.postgresql.util.PSQLException

object UpdateMemberDAO {

    fun promote(data: MemberOperator): Status {
        return try {
            when(data.community){
                true-> if(transaction { !CommunityMember.select { (CommunityMember.communityID eq data.subjectID) and (CommunityMember.userID eq data.userID) and (CommunityMember.admin eq true)}.empty() })
                            transaction {
                                CommunityMember.update ({(CommunityMember.userID eq data.memberID) and (CommunityMember.communityID eq data.subjectID) and (CommunityMember.admin eq false)}){
                                it[admin] = true
                            }
                        }

                false-> if(transaction { !GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty() })
                            transaction {
                                GroupMember.update ({(GroupMember.userID eq data.memberID) and (GroupMember.groupID eq data.subjectID) and (GroupMember.admin eq false)}){
                                it[admin] = true
                            }
                        }

            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    fun lower(data: MemberOperator): Status {
        return try {
            when(data.community){
                true-> if(transaction { !CommunityMember.select { (CommunityMember.communityID eq data.subjectID) and (CommunityMember.userID eq data.userID) and (CommunityMember.admin eq true)}.empty() })
                            transaction {
                                CommunityMember.update ({(CommunityMember.userID eq data.memberID) and (CommunityMember.communityID eq data.subjectID) and (CommunityMember.admin eq true)}){
                                it[admin] = false
                            }
                        }

                false-> if(transaction { !GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty() })
                            transaction {
                                GroupMember.update ({(GroupMember.userID eq data.memberID) and (GroupMember.groupID eq data.subjectID) and (GroupMember.admin eq true)}){
                                it[admin] = false
                            }
                        }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}