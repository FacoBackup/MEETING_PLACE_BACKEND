package br.meetingplace.server.modules.group.routes


import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.community.services.validation.CommunityValidationService
import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.group.dao.member.GroupMemberDAO
import br.meetingplace.server.modules.group.dto.requests.RequestGroup
import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation
import br.meetingplace.server.modules.group.dto.requests.RequestGroupMember
import br.meetingplace.server.modules.group.services.delete.GroupDeleteService
import br.meetingplace.server.modules.group.services.factory.GroupFactoryService
import br.meetingplace.server.modules.group.services.member.GroupMemberService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.groupRouter() {
    route("/api") {

        post<RequestGroupCreation>(GroupPaths.GROUP) {
            val log = call.log
            if(log != null)
                call.respond(GroupFactoryService.create(requester = log.userID,data = it, communityMemberDAO = CommunityMemberDAO, groupDAO = GroupDAO,userDAO =UserDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete(GroupPaths.GROUP) {
            val data = call.receive<RequestGroup>()
            val log = call.log
            if(log != null)
                call.respond(GroupDeleteService.delete(requester = log.userID, data, GroupMemberDAO,GroupDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        patch(GroupPaths.MEMBER) {
            val data = call.receive<RequestGroupMember>()
            val log = call.log
            if(log != null)
                call.respond(GroupMemberService.addMember(requester = log.userID,data, GroupMemberDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete(GroupPaths.MEMBER) {
            val data = call.receive<RequestGroupMember>()
            val log = call.log
            if(log != null)
                call.respond(GroupMemberService.removeMember(requester = log.userID, data, GroupMemberDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
    }
}