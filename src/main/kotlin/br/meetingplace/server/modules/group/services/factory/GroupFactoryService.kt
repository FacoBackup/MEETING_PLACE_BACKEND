package br.meetingplace.server.modules.group.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation

object GroupFactoryService {

    fun create(data: RequestGroupCreation, communityMemberDAO: CMI, groupDAO: GI, userDAO: UI) : Status {
        return try {
            return when(data.communityID.isNullOrBlank()){
                true-> {
                    if (userDAO.read(data.userID) != null) //user
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