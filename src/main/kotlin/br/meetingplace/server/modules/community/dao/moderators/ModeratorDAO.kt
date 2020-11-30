package br.meetingplace.server.modules.community.dao.moderators

import br.meetingplace.server.db.mapper.group.GroupMapperInterface
import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.group.db.Group
import br.meetingplace.server.requests.community.Approval
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object ModeratorDAO {
    fun approveGroup(data: Approval, groupMapper: GroupMapperInterface): Status {
        return try{
            val group = transaction {
                Group.select { (Group.id eq data.serviceID) and (Group.communityID eq data.communityID) }.map { groupMapper.mapGroup(it) }
            }.firstOrNull()

            if(transaction { CommunityMember.select { (CommunityMember.userID eq data.userID) and (CommunityMember.communityID eq data.communityID) and (CommunityMember.admin eq true) } }.firstOrNull() != null &&
                group != null && !group.approved){
                transaction {
                    Group.update ({Group.id eq data.serviceID}) {
                        it[approved] = true
                    }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}