package br.meetingplace.server.routers.groups


import br.meetingplace.server.db.mapper.community.CommunityMapper
import br.meetingplace.server.db.mapper.group.GroupMapper
import br.meetingplace.server.modules.group.dao.delete.GroupDeleteDAO
import br.meetingplace.server.modules.group.dao.factory.GroupFactoryDAO
import br.meetingplace.server.modules.group.dao.member.MemberDAO
import br.meetingplace.server.requests.community.RequestCommunityCreation
import br.meetingplace.server.requests.generic.MemberOperator
import br.meetingplace.server.requests.generic.SimpleOperator
import br.meetingplace.server.requests.group.RequestGroupCreation
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.groupRouter() {
    route("/api") {

        post(GroupPaths.GROUP) {
            val data = call.receive<RequestGroupCreation>()
            call.respond(GroupFactoryDAO.create(data, communityMapper = CommunityMapper))
        }
        delete(GroupPaths.GROUP) {
            val data = call.receive<SimpleOperator>()
            call.respond(GroupDeleteDAO.delete(data, groupMapper = GroupMapper))
        }
        patch(GroupPaths.MEMBER) {
            val data = call.receive<MemberOperator>()
            call.respond(MemberDAO.addMember(data))
        }
        delete(GroupPaths.MEMBER) {
            val data = call.receive<MemberOperator>()
            call.respond(MemberDAO.removeMember(data))
        }
    }
}