package br.meetingplace.server.modules.community.service.member

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.user.dao.UI
import br.meetingplace.server.request.dto.community.RequestCommunityMemberDTO
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages

object CommunityMemberService {

    fun promoteMember(data: RequestCommunityMemberDTO, communityMemberDAO: CMI, userDAO: UI): Status{
        return try {
            val userMember = communityMemberDAO.read(communityID = data.communityID, userID = data.userID)
            val member = communityMemberDAO.read(communityID = data.communityID, userID = data.memberID)
            return if(member != null){
                when(member.role){
                    MemberType.LEADER.toString()->{
                        if(userMember != null && userMember.role == MemberType.DIRECTOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.DIRECTOR.toString())
                        else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }
                    MemberType.MEMBER.toString()->{
                        if(userMember != null && userMember.role == MemberType.DIRECTOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.LEADER.toString())
                        else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }
                    MemberType.FOLLOWER.toString()->{
                        if(userMember != null && (userMember.role == MemberType.DIRECTOR.toString() || userMember.role == MemberType.LEADER.toString()))
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.MEMBER.toString())
                        else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }
                    else-> Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
            }
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun lowerMember(data: RequestCommunityMemberDTO, communityMemberDAO: CMI, userDAO: UI): Status{
        return try {
            val userMember = communityMemberDAO.read(communityID = data.communityID, userID = data.userID)
            val member = communityMemberDAO.read(communityID = data.communityID, userID = data.memberID)
            return if(member != null){
                when(member.role){
                    MemberType.LEADER.toString()->{
                        if(userMember != null && userMember.role == MemberType.DIRECTOR.toString())
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.MEMBER.toString())
                        else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }
                    MemberType.MEMBER.toString()->{
                        if(userMember != null && (userMember.role == MemberType.DIRECTOR.toString() || userMember.role == MemberType.LEADER.toString()))
                            communityMemberDAO.update(userID = data.memberID, communityID = data.memberID, role = MemberType.FOLLOWER.toString())
                        else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }
                    else-> Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                }
            }
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}