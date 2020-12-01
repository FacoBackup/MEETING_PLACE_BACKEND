package br.meetingplace.server.routers.groups


import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.group.service.delete.GroupDelete
import br.meetingplace.server.modules.group.service.factory.GroupFactory
import br.meetingplace.server.modules.group.service.member.GroupMember
import br.meetingplace.server.request.dto.generic.MemberDTO
import br.meetingplace.server.request.dto.generic.SubjectDTO
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.groupRouter() {
    route("/api") {

        post(GroupPaths.GROUP) {
            val data = call.receive<GroupCreationDTO>()
            call.respond(GroupFactory.create(data, communityMapper = CommunityDAO))
        }
        delete(GroupPaths.GROUP) {
            val data = call.receive<SubjectDTO>()
            call.respond(GroupDelete.delete(data, groupMapper = GroupDAO))
        }
        patch(GroupPaths.MEMBER) {
            val data = call.receive<MemberDTO>()
            call.respond(GroupMember.addMember(data))
        }
        delete(GroupPaths.MEMBER) {
            val data = call.receive<MemberDTO>()
            call.respond(GroupMember.removeMember(data))
        }
    }
}