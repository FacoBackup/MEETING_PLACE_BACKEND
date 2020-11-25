package br.meetingplace.server.modules.groups.dao.delete

import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.requests.generic.operators.SimpleOperator

object GroupDelete{
    fun delete(data: SimpleOperator): Status {
        TODO("NOT YET IMPLEMENTED")
//        val group = groupDB.select(data.identifier.ID)
//        lateinit var members: List<MemberData>
//        lateinit var groups: List<String>
//        lateinit var userGroups: List<String>
//
//        return when (data.identifier.community && !data.identifier.owner.isNullOrBlank()) {
//            true -> {
//                val community = communityDB.select(data.identifier.owner)
//                if (group != null && community != null && group.getApproved() && userDB.check(data.login.email) && getMemberRole(group.getMembers(), data.login.email) == MemberType.MODERATOR) {
//                    val chat = chatDB.select(group.getChatID())
//                    members = group.getMembers()
//
//                    for (i in members) {
//                        val member = userDB.select(i.ID)
//                        if (member != null && getMemberRole(group.getMembers(), i.ID) != null) {
//                            userGroups = member.getGroups()
//                            userGroups.remove(group.getID())
//                            member.setGroups(userGroups)
//                            userDB.insert(member)
//                        }
//                    }
//
//                    groups = community.getGroups()
//                    groups.remove(group.getID())
//                    community.setGroups(groups)
//
//                    if (chat != null)
//                        chatDB.delete(chat)
//                    communityDB.insert(community)
//                    return groupDB.delete(group)
//                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
//            }
//            false -> {
//                if (group != null && userDB.check(data.login.email) && getMemberRole(group.getMembers(), data.login.email) == MemberType.MODERATOR) {
//                    val chat = chatDB.select(group.getChatID())
//                    members = group.getMembers()
//
//                    for (i in members) {
//                        val member = userDB.select(i.ID)
//                        if (member != null && getMemberRole(group.getMembers(), i.ID) != null) {
//                            userGroups = member.getGroups()
//                            userGroups.remove(group.getID())
//                            member.setGroups(userGroups)
//                        }
//                    }
//
//                    if (chat != null)
//                        chatDB.delete(chat)
//
//                    return groupDB.delete(group)
//                }else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
//            }
//        }
    }
}