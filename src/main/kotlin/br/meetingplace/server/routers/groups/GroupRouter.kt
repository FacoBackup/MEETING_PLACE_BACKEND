package br.meetingplace.server.routers.groups


import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.group.dao.GroupMapper
import br.meetingplace.server.modules.group.service.delete.GroupDeleteDAO
import br.meetingplace.server.modules.group.service.factory.GroupFactoryDAO
import br.meetingplace.server.modules.group.service.member.MemberDAO
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
            call.respond(GroupFactoryDAO.create(data, communityMapper = CommunityDAO))
        }
        delete(GroupPaths.GROUP) {
            val data = call.receive<SubjectDTO>()
            call.respond(GroupDeleteDAO.delete(data, groupMapper = GroupMapper))
        }
        patch(GroupPaths.MEMBER) {
            val data = call.receive<MemberDTO>()
            call.respond(MemberDAO.addMember(data))
        }
        delete(GroupPaths.MEMBER) {
            val data = call.receive<MemberDTO>()
            call.respond(MemberDAO.removeMember(data))
        }
    }
}