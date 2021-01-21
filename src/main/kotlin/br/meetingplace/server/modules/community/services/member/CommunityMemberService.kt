package br.meetingplace.server.modules.community.services.member

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityMember
import io.ktor.http.*

object CommunityMemberService {

    suspend fun promoteMember(requester: Long,data: RequestCommunityMember, communityMemberDAO: CMI): HttpStatusCode{
        return try {
            val userMember = communityMemberDAO.read(communityID = data.communityID, userID = requester)
            val member = communityMemberDAO.read(communityID = data.communityID, userID = data.memberID)
            return if(member != null){
                when(member.role){
                    MemberType.MEMBER.toString()->{
                        if(userMember != null && userMember.role == MemberType.MODERATOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.MODERATOR.toString())
                        else HttpStatusCode.FailedDependency
                    }
                    MemberType.FOLLOWER.toString()->{
                        if(userMember != null && (userMember.role == MemberType.MODERATOR.toString() || userMember.role == MemberType.MEMBER.toString()))
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.MEMBER.toString())
                        else HttpStatusCode.FailedDependency
                    }
                    else-> HttpStatusCode.FailedDependency
                }
            }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    suspend  fun lowerMember(requester: Long,data: RequestCommunityMember, communityMemberDAO: CMI): HttpStatusCode{
        return try {
            val userMember = communityMemberDAO.read(communityID = data.communityID, userID = requester)
            val member = communityMemberDAO.read(communityID = data.communityID, userID = data.memberID)
            return if(member != null){
                when(member.role){
                    MemberType.MODERATOR.toString()->{
                        if(userMember != null && userMember.role == MemberType.MODERATOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.communityID, role = MemberType.MEMBER.toString())
                        else HttpStatusCode.FailedDependency
                    }
                    MemberType.MEMBER.toString()->{
                        if(userMember != null && userMember.role == MemberType.MODERATOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.communityID, role = MemberType.FOLLOWER.toString())
                        else HttpStatusCode.FailedDependency
                    }
                    else-> HttpStatusCode.FailedDependency
                }
            }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
    suspend  fun removeMember(requester: Long,data: RequestCommunityMember, communityMemberDAO: CMI): HttpStatusCode{
        return try {
            val requesterMember = communityMemberDAO.read(communityID = data.communityID, userID = requester)
            val member = communityMemberDAO.read(communityID = data.communityID, userID = data.memberID)
            return if(requesterMember != null){
                if(MemberType.MODERATOR.toString() == requesterMember.role && member != null && member.role != MemberType.MODERATOR.toString()){
                    communityMemberDAO.delete(userID = data.memberID, communityID = data.communityID)
                }
                else
                    HttpStatusCode.ServiceUnavailable
            }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}