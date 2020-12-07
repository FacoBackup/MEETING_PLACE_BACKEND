package br.meetingplace.server.modules.group.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object GroupFactoryService {

    fun create(requester: String,data: RequestGroupCreation, communityMemberDAO: CMI, groupDAO: GI, userDAO: UI) : HttpStatusCode {
        return try {
            return when(data.communityID.isNullOrBlank()){
                true-> {
                    if (userDAO.check(requester)) //user
                        groupDAO.create(data, approved = true)
                    else HttpStatusCode.FailedDependency
                }
                false->{ //community
                    val member = communityMemberDAO.read(data.communityID, userID = requester)
                    if(member != null)
                        groupDAO.create(data, approved = member.role == MemberType.DIRECTOR.toString() || member.role == MemberType.LEADER.toString())
                    else HttpStatusCode.FailedDependency
                }
            }
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }

}