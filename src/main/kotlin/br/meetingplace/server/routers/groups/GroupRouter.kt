package br.meetingplace.server.routers.groups


import br.meetingplace.server.modules.communityTODO.dao.CommunityDAO
import br.meetingplace.server.modules.groupTODO.dao.GroupDAO
import br.meetingplace.server.modules.groupTODO.service.delete.GroupDeleteService
import br.meetingplace.server.modules.groupTODO.service.factory.GroupFactoryService
import br.meetingplace.server.modules.groupTODO.service.member.GroupMemberService
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
            call.respond(GroupFactoryService.create(data, communityMapper = CommunityDAO))
        }
        delete(GroupPaths.GROUP) {
            val data = call.receive<SubjectDTO>()
            call.respond(GroupDeleteService.delete(data, groupMapper = GroupDAO))
        }
        patch(GroupPaths.MEMBER) {
            val data = call.receive<MemberDTO>()
            call.respond(GroupMemberService.addMember(data))
        }
        delete(GroupPaths.MEMBER) {
            val data = call.receive<MemberDTO>()
            call.respond(GroupMemberService.removeMember(data))
        }
    }
}