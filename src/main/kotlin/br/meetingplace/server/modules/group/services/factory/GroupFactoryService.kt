package br.meetingplace.server.modules.group.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation
import io.ktor.http.*

object GroupFactoryService {

    fun create(data: RequestGroupCreation, communityMemberDAO: CMI, groupDAO: GI, userDAO: UI) : HttpStatusCode {
        return try {
            return when(data.communityID.isNullOrBlank()){
                true-> {
                    if (userDAO.check(data.userID) == HttpStatusCode.Found) //user
                        groupDAO.create(data, approved = true)
                    else HttpStatusCode.FailedDependency
                }
                false->{ //community
                    val member = communityMemberDAO.read(data.communityID, data.userID)
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