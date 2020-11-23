package br.meetingplace.server.modules.members.dao.add

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.operators.MemberOperator

class AddMember private constructor(){
    companion object{
        private val Class = AddMember()
        fun getClass () = Class
    }
    fun addMember(data: MemberOperator, communityDB: CommunityDBInterface, groupDB: GroupDBInterface, userDB: UserDBInterface): Status {
        lateinit var members: List<MemberData>
        lateinit var userMemberIn: List<String>
        return when(data.identifier.community){
            true->{
                val community = communityDB.select(data.identifier.ID)
                val newMember = userDB.select(data.memberEmail)
                if(userDB.check(data.login.email) && newMember != null && community != null && getMemberRole(community.getMembers(), newMember.getEmail()) == null &&getMemberRole(community.getMembers(), data.login.email) != null){
                    members = community.getMembers()
                    members.add(MemberData(newMember.getEmail(), MemberType.NORMAL))
                    community.setMembers(members)

                    userMemberIn = newMember.getCommunities()
                    userMemberIn.add(community.getID())
                    newMember.setCommunities(userMemberIn)

                    communityDB.insert(community)
                    return userDB.insert(newMember)
                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
            false->{
                val group = groupDB.select(data.identifier.ID)
                val newMember = userDB.select(data.memberEmail)
                if(userDB.check(data.login.email) && newMember != null && group != null && getMemberRole(group.getMembers(), newMember.getEmail()) == null && getMemberRole(group.getMembers(), data.login.email) != null && group.getApproved()){
                    members = group.getMembers()
                    members.add(MemberData(newMember.getEmail(), MemberType.NORMAL))
                    group.setMembers(members)

                    userMemberIn = newMember.getGroups()
                    userMemberIn.add(group.getID())
                    newMember.setGroups(userMemberIn)


                    groupDB.insert(group)
                    return userDB.insert(newMember)
                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
        }
    }
}