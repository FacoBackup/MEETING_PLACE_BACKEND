package br.meetingplace.server.modules.group.service.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.type.MemberType
import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object GroupFactory {

    fun create(data: GroupCreationDTO, communityMemberDAO: CMI, groupDAO: GI) : Status {
        return try {
            return when(data.communityID.isNullOrBlank()){
                true-> {
                    if (transaction { !User.select { User.id eq data.userID }.empty() }) //user
                        groupDAO.create(data, approved = true)
                    else Status(404, StatusMessages.NOT_FOUND)
                }
                false->{ //community
                    val member = communityMemberDAO.read(data.communityID, data.userID)
                    if(member != null)
                        groupDAO.create(data, approved = member.role == MemberType.DIRECTOR.toString() || member.role == MemberType.LEADER.toString())
                    else Status(404, StatusMessages.NOT_FOUND)
                }
            }
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

}