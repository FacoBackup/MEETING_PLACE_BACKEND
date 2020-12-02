package br.meetingplace.server.modules.communityTODO.service.member

object CommunityMemberService {
    fun promoteMember(){
        TODO("not yet implemented")
//        if(transaction { !GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty() })
//            transaction {
//                GroupMember.update ({(GroupMember.userID eq data.memberID) and (GroupMember.groupID eq data.subjectID) and (GroupMember.admin eq false)}){
//                    it[admin] = true
//                }
//            }
    }
    fun lowerMember(){
        TODO("not yet implemented")
//        if(transaction { !GroupMember.select { (GroupMember.groupID eq data.subjectID) and (GroupMember.userID eq data.userID) and (GroupMember.admin eq true)}.empty() })
//            transaction {
//                GroupMember.update ({(GroupMember.userID eq data.memberID) and (GroupMember.groupID eq data.subjectID) and (GroupMember.admin eq true)}){
//                    it[admin] = false
//                }
//            }
    }
    fun addMember(){
        TODO("not yet implemented")
    }
    fun removeMember(){
        TODO("not yet implemented")
    }
}