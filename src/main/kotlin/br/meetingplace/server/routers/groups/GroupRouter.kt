package br.meetingplace.server.routers.groups


import br.meetingplace.server.db.mapper.community.CommunityMapper
import br.meetingplace.server.db.mapper.group.GroupMapper
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dao.delete.GroupDelete
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dao.factory.GroupFactory
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
            call.respond(GroupFactory.create(group, communityMapper = CommunityMapper))
        }
        delete(GroupPaths.GROUP) {
            val group = call.receive<SimpleOperator>()
            call.respond(GroupDelete.delete(group, groupMapper = GroupMapper))
        }
        patch(GroupPaths.MEMBER) {
            TODO("NOT YET IMPLEMENTED")
//            val member = call.receive<MemberOperator>()
//            call.respond(GroupMembers.addMember(member, userDB = UserRW.getClass(), groupDB = GroupRW.getClass(), communityDB = CommunityRW.getClass()))
        }
        delete(GroupPaths.MEMBER) {
            TODO("NOT YET IMPLEMENTED")
//            val member = call.receive<MemberOperator>()
//            call.respond(GroupMembers.removeMember(member, userDB = UserRW.getClass(), groupDB = GroupRW.getClass(), communityDB = CommunityRW.getClass()))
        }
    }
}