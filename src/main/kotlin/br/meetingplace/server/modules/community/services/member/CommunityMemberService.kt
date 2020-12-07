package br.meetingplace.server.modules.community.services.member

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityMember
import io.ktor.http.*

object CommunityMemberService {

    fun promoteMember(requester: String,data: RequestCommunityMember, communityMemberDAO: CMI): HttpStatusCode{
        return try {
            val userMember = communityMemberDAO.read(communityID = data.communityID, userID = requester)
            val member = communityMemberDAO.read(communityID = data.communityID, userID = data.memberID)
            return if(member != null){
                when(member.role){
                    MemberType.LEADER.toString()->{
                        if(userMember != null && userMember.role == MemberType.DIRECTOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.DIRECTOR.toString())
                        else HttpStatusCode.FailedDependency
                    }
                    MemberType.MEMBER.toString()->{
                        if(userMember != null && userMember.role == MemberType.DIRECTOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.LEADER.toString())
                        else HttpStatusCode.FailedDependency
                    }
                    MemberType.FOLLOWER.toString()->{
                        if(userMember != null && (userMember.role == MemberType.DIRECTOR.toString() || userMember.role == MemberType.LEADER.toString()))
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

    fun lowerMember(requester: String,data: RequestCommunityMember, communityMemberDAO: CMI): HttpStatusCode{
        return try {
            val userMember = communityMemberDAO.read(communityID = data.communityID, userID = requester)
            val member = communityMemberDAO.read(communityID = data.communityID, userID = data.memberID)
            return if(member != null){
                when(member.role){
                    MemberType.LEADER.toString()->{
                        if(userMember != null && userMember.role == MemberType.DIRECTOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.MEMBER.toString())
                        else HttpStatusCode.FailedDependency
                    }
                    MemberType.MEMBER.toString()->{
                        if(userMember != null && (userMember.role == MemberType.DIRECTOR.toString() || userMember.role == MemberType.LEADER.toString()))
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.FOLLOWER.toString())
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
}