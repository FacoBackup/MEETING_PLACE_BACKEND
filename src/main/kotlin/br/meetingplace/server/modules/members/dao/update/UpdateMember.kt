package br.meetingplace.server.modules.members.dao.update

import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.groups.db.GroupMember
import br.meetingplace.server.requests.generic.operators.MemberOperator
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

object UpdateMember {

    fun promote(data: MemberOperator): Status {
        return try {
            when(data.community){
                true->{
                    if(!CommunityMember.select { (CommunityMember.communityID eq data.subjectID) and (CommunityMember.userID eq data.userID) and (CommunityMember.admin eq true)}.empty())
                        CommunityMember.update ({(CommunityMember.userID eq data.memberEmail) and (CommunityMember.communityID eq data.subjectID) and (CommunityMember.admin eq false)}){
                            it[admin] = true
                        }
                }
                false->{
                    if(!GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty())
                        GroupMember.update ({(GroupMember.userID eq data.memberEmail) and (GroupMember.groupID eq data.subjectID) and (GroupMember.admin eq false)}){
                            it[admin] = true
                        }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    fun lower(data: MemberOperator): Status {
        return try {
            when(data.community){
                true->{
                    if(!CommunityMember.select { (CommunityMember.communityID eq data.subjectID) and (CommunityMember.userID eq data.userID) and (CommunityMember.admin eq true)}.empty())
                        CommunityMember.update ({(CommunityMember.userID eq data.memberEmail) and (CommunityMember.communityID eq data.subjectID) and (CommunityMember.admin eq true)}){
                            it[admin] = false
                        }
                }
                false->{
                    if(!GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty())
                        GroupMember.update ({(GroupMember.userID eq data.memberEmail) and (GroupMember.groupID eq data.subjectID) and (GroupMember.admin eq true)}){
                            it[admin] = false
                        }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}