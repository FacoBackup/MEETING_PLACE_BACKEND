package br.meetingplace.server.routers.groups


import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.group.services.delete.GroupDeleteService
import br.meetingplace.server.modules.group.services.factory.GroupFactoryService
import br.meetingplace.server.modules.group.services.member.GroupMemberService
import br.meetingplace.server.modules.group.dto.requests.RequestGroupMember
import br.meetingplace.server.modules.group.dto.requests.RequestGroup
import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.groupRouter() {
    route("/api") {

        post(GroupPaths.GROUP) {
            val data = call.receive<RequestGroupCreation>()
            call.respond(GroupFactoryService.create(data, communityMapper = CommunityDAO))
        }
        delete(GroupPaths.GROUP) {
            val data = call.receive<RequestGroup>()
            call.respond(GroupDeleteService.delete(data, groupMapper = GroupDAO))
        }
        patch(GroupPaths.MEMBER) {
            val data = call.receive<RequestGroupMember>()
            call.respond(GroupMemberService.addMember(data))
        }
        delete(GroupPaths.MEMBER) {
            val data = call.receive<RequestGroupMember>()
            call.respond(GroupMemberService.removeMember(data))
        }
    }
}