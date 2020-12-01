package br.meetingplace.server.modules.community.service.moderators

import br.meetingplace.server.modules.group.dao.GroupMapperInterface
import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.group.entitie.Group
import br.meetingplace.server.request.dto.community.ApprovalDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object ModeratorDAO {
    fun approveGroup(data: ApprovalDTO, groupMapper: GroupMapperInterface): Status {
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