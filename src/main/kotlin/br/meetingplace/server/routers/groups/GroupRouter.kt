package br.meetingplace.server.routers.groups

import br.meetingplace.server.db.chat.file.ChatRW
import br.meetingplace.server.db.community.file.CommunityRW
import br.meetingplace.server.db.group.file.GroupRW
import br.meetingplace.server.db.user.file.UserRW
import br.meetingplace.server.modules.groups.dao.delete.GroupDelete
import br.meetingplace.server.modules.groups.dao.factory.GroupFactory
import br.meetingplace.server.modules.groups.dao.members.GroupMembers
import br.meetingplace.server.requests.generic.CreationData
import br.meetingplace.server.requests.generic.MemberOperator
import br.meetingplace.server.requests.generic.SimpleOperator
import br.meetingplace.server.routers.groups.paths.GroupPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.GroupRouter (){
    route("/api"){

        post(GroupPaths.GROUP) {
            val group = call.receive<CreationData>()
            call.respond(GroupFactory.getClass().create(group,rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
        }
        delete(GroupPaths.GROUP) {
            val group = call.receive<SimpleOperator>()
            call.respond(GroupDelete.getClass().delete(group,rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
        }
        patch(GroupPaths.MEMBER) {
            val member = call.receive<MemberOperator>()
            call.respond(GroupMembers.getClass().addMember(member, rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass()))
        }
        delete(GroupPaths.MEMBER) {
            val member = call.receive<MemberOperator>()
            call.respond(GroupMembers.getClass().removeMember(member, rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass()))
        }
    }
}