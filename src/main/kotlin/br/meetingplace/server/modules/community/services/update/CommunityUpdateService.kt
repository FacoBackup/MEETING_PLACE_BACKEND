package br.meetingplace.server.modules.community.services.update

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityUpdate
import org.postgresql.util.PSQLException

object CommunityUpdateService {
    fun updateCommunity(data: RequestCommunityUpdate, community: CI, member: CMI): Status{
        return try {
            val memberData = member.read(communityID = data.communityID, userID = data.userID)

            if(memberData != null &&
              (memberData.role == MemberType.LEADER.toString() ||
               memberData.role == MemberType.DIRECTOR.toString() ||
               memberData.role == MemberType.MEMBER.toString())){

               community.update(communityID = data.communityID, name = data.name, about = data.about, parentID = data.parentCommunityID)
            }else Status(401, StatusMessages.UNAUTHORIZED)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}