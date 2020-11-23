package br.meetingplace.server.modules.members.dao.update

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.global.methods.member.getMemberRole
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType
import br.meetingplace.server.requests.generic.operators.MemberOperator

class UpdateMember private constructor(){
    companion object{
        private val Class = UpdateMember()
        fun getClass () = Class
    }
    fun promote(data: MemberOperator, communityDB: CommunityDBInterface, groupDB: GroupDBInterface, userDB: UserDBInterface):Status{
        lateinit var members: List<MemberData>
        return when(data.identifier.community){
            true->{
                val community = communityDB.select(data.identifier.ID)
                if(userDB.check(data.login.email) && userDB.check(data.memberEmail) && community != null && getMemberRole(community.getMembers(),data.memberEmail) == MemberType.NORMAL && getMemberRole(community.getMembers(), data.login.email)  == MemberType.MODERATOR){
                    members = community.getMembers()
                    members.remove(MemberData(data.memberEmail, MemberType.NORMAL))
                    members.add(MemberData(data.memberEmail, MemberType.MODERATOR))
                    community.setMembers(members)

                    return communityDB.insert(community)
                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
            false->{
                val group = groupDB.select(data.identifier.ID)
                if(userDB.check(data.login.email) && userDB.check(data.memberEmail) && group != null && getMemberRole(group.getMembers(), data.memberEmail) == MemberType.NORMAL && getMemberRole(group.getMembers(), data.login.email)  == MemberType.MODERATOR && group.getApproved()){
                    members = group.getMembers()
                    members.remove(MemberData(data.memberEmail, MemberType.NORMAL))
                    members.add(MemberData(data.memberEmail, MemberType.MODERATOR))
                    group.setMembers(members)

                    return groupDB.insert(group)
                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
        }
    }
    fun lower(data: MemberOperator, communityDB: CommunityDBInterface, groupDB: GroupDBInterface, userDB: UserDBInterface): Status {
        lateinit var members: List<MemberData>
        return when(data.identifier.community){
            true->{
                val community = communityDB.select(data.identifier.ID)
                if(userDB.check(data.login.email) && userDB.check(data.memberEmail) && community != null && getMemberRole(community.getMembers(),data.memberEmail) == MemberType.MODERATOR && getMemberRole(community.getMembers(), data.login.email)  == MemberType.MODERATOR){
                    members = community.getMembers()
                    members.remove(MemberData(data.memberEmail, MemberType.MODERATOR))
                    members.add(MemberData(data.memberEmail, MemberType.NORMAL))
                    community.setMembers(members)

                    return communityDB.insert(community)
                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
            false->{
                val group = groupDB.select(data.identifier.ID)
                if(userDB.check(data.login.email) && userDB.check(data.memberEmail) && group != null && getMemberRole(group.getMembers(), data.memberEmail) == MemberType.MODERATOR && getMemberRole(group.getMembers(), data.login.email)  == MemberType.MODERATOR && group.getApproved()){
                    members = group.getMembers()
                    members.remove(MemberData(data.memberEmail, MemberType.MODERATOR))
                    members.add(MemberData(data.memberEmail, MemberType.NORMAL))
                    group.setMembers(members)

                    return groupDB.insert(group)
                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
            }
        }
    }
}