package br.meetingplace.server.modules.members.dao.remove

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.operators.MemberOperator

class RemoveMember private constructor(){
    companion object{
        private val Class = RemoveMember()
        fun getClass () = Class
    }
    fun removeMember(data: MemberOperator, communityDB: CommunityDBInterface, groupDB: GroupDBInterface, userDB: UserDBInterface): Status {
        lateinit var members: List<MemberData>
        lateinit var userMemberIn: List<String>
        return when(data.identifier.community){
            true->{
                val community = communityDB.select(data.identifier.ID)
                val member = userDB.select(data.memberEmail)
                if(userDB.check(data.login.email) && member != null && community != null && getMemberRole(community.getMembers(), member.getEmail()) == MemberType.NORMAL && getMemberRole(community.getMembers(), data.login.email)  == MemberType.MODERATOR){
                    members = community.getMembers()
                    members.remove(MemberData(member.getEmail(), MemberType.NORMAL))
                    community.setMembers(members)

                    userMemberIn = member.getCommunities()
                    userMemberIn.remove(community.getID())
                    member.setCommunities(userMemberIn)

                    communityDB.insert(community)
                    return userDB.insert(member)
                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
            false->{
                val group = groupDB.select(data.identifier.ID)
                val member = userDB.select(data.memberEmail)
                if(userDB.check(data.login.email) && member != null && group != null && getMemberRole(group.getMembers(), member.getEmail()) == MemberType.NORMAL && getMemberRole(group.getMembers(), data.login.email)  == MemberType.MODERATOR && group.getApproved()){
                    members = group.getMembers()
                    members.remove(MemberData(member.getEmail(), MemberType.NORMAL))
                    group.setMembers(members)

                    userMemberIn = member.getGroups()
                    userMemberIn.remove(group.getID())
                    member.setGroups(userMemberIn)

                    groupDB.insert(group)
                    return userDB.insert(member)

                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
        }
    }
}