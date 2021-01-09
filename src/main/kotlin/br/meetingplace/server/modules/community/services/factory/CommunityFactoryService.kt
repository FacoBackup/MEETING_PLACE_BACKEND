package br.meetingplace.server.modules.community.services.factory

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object CommunityFactoryService {

    suspend fun create(requester: String,data: RequestCommunityCreation, communityDAO: CI, communityMemberDAO: CMI): HttpStatusCode {

        return try {
            return if(!data.relatedCommunityID.isNullOrBlank() && communityDAO.read(data.relatedCommunityID) != null){
                communityDAO.create(data)
                val community = communityDAO.readByExactName(data.name)
                return if(community != null)
                    communityMemberDAO.create(userID = requester, communityID = community.id, role = MemberType.MODERATOR.toString())
                else
                    HttpStatusCode.InternalServerError
            }
            else if(data.relatedCommunityID.isNullOrBlank()){
                communityDAO.create(data)
                val community = communityDAO.readByExactName(data.name)
                return if(community != null)
                    communityMemberDAO.create(userID = requester, communityID = community.id, role = MemberType.MODERATOR.toString())
                else
                    HttpStatusCode.InternalServerError
            }
            else {
                HttpStatusCode.InternalServerError
            }

        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}