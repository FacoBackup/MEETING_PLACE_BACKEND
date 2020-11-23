package br.meetingplace.server.modules.members.dao.remove

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.operators.MemberOperator

class RemoveMember private constructor(){
    companion object{
        private val Class = RemoveMember()
        fun getClass () = Class
    }
    fun removeMember(data: MemberOperator, communityDB: CommunityDBInterface, groupDB: GroupDBInterface, userDB: UserDBInterface){
        lateinit var members: List<MemberData>
        lateinit var userMemberIn: List<String>
        when(data.identifier.community){
            true->{
                val community = communityDB.select(data.identifier.ID)
                val newMember = userDB.select(data.memberEmail)
                if(userDB.check(data.login.email) && newMember != null && community != null && getMemberRole(community.getMembers(), newMember.getEmail()) == MemberType.NORMAL && getMemberRole(community.getMembers(), data.login.email)  == MemberType.MODERATOR){
                    members = community.getMembers()
                    members.remove(MemberData(newMember.getEmail(), MemberType.NORMAL))
                    community.setMembers(members)

                    userMemberIn = newMember.getCommunities()
                    userMemberIn.remove(community.getID())
                    newMember.setCommunities(userMemberIn)

                    communityDB.insert(community)
                    userDB.insert(newMember)
                }
            }
            false->{
                val group = groupDB.select(data.identifier.ID)
                val newMember = userDB.select(data.memberEmail)
                if(userDB.check(data.login.email) && newMember != null && group != null && getMemberRole(group.getMembers(), newMember.getEmail()) == MemberType.NORMAL && getMemberRole(group.getMembers(), data.login.email)  == MemberType.MODERATOR && group.getApproved()){
                    members = group.getMembers()
                    members.remove(MemberData(newMember.getEmail(), MemberType.NORMAL))
                    group.setMembers(members)

                    userMemberIn = newMember.getGroups()
                    userMemberIn.remove(group.getID())
                    newMember.setGroups(userMemberIn)

                    groupDB.insert(group)
                    userDB.insert(newMember)
                }
            }
        }
    }
}