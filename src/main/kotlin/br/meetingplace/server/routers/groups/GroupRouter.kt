package br.meetingplace.server.routers.groups

import br.meetingplace.server.db.chat.file.ChatRW
import br.meetingplace.server.db.community.file.CommunityRW
import br.meetingplace.server.db.group.file.GroupRW
import br.meetingplace.server.db.user.file.UserRW
import br.meetingplace.server.modules.groups.dao.delete.GroupDelete
import br.meetingplace.server.modules.groups.dao.factory.GroupFactory
import br.meetingplace.server.requests.generic.data.CreationData
import br.meetingplace.server.requests.generic.operators.SimpleOperator
import br.meetingplace.server.routers.groups.paths.GroupPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.groupRouter() {
    route("/api") {

        post(GroupPaths.GROUP) {
            val group = call.receive<CreationData>()
            call.respond(GroupFactory.getClass().create(group, userDB = UserRW.getClass(), groupDB = GroupRW.getClass(), communityDB = CommunityRW.getClass(), chatDB = ChatRW.getClass()))
        }
        delete(GroupPaths.GROUP) {
            val group = call.receive<SimpleOperator>()
            call.respond(GroupDelete.getClass().delete(group, userDB = UserRW.getClass(), groupDB = GroupRW.getClass(), communityDB = CommunityRW.getClass(), chatDB = ChatRW.getClass()))
        }
        patch(GroupPaths.MEMBER) {
            TODO("NOT YET IMPLEMENTED")
//            val member = call.receive<MemberOperator>()
//            call.respond(GroupMembers.getClass().addMember(member, userDB = UserRW.getClass(), groupDB = GroupRW.getClass(), communityDB = CommunityRW.getClass()))
        }
        delete(GroupPaths.MEMBER) {
            TODO("NOT YET IMPLEMENTED")
//            val member = call.receive<MemberOperator>()
//            call.respond(GroupMembers.getClass().removeMember(member, userDB = UserRW.getClass(), groupDB = GroupRW.getClass(), communityDB = CommunityRW.getClass()))
        }
    }
}