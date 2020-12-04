package br.meetingplace.server.routers.community

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.community.services.factory.CommunityFactoryService
import br.meetingplace.server.modules.community.services.approval.CommunityApprovalService
import br.meetingplace.server.modules.community.dto.requests.RequestApproval
import br.meetingplace.server.modules.community.dto.requests.RequestCommunity
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.communityRouter() {
    route("/api") {
//        get (CommunityPaths.MEMBER){
//            val data= call.receive<SimpleOperator>()
//            val communities = transaction { Community.select { Community.id eq data.subjectID }.map { CommunityMapper.mapCommunityDTO(it) } }
//            if(communities.isEmpty())
//                call.respond(Status(404, StatusMessages.NOT_FOUND))
//            else
//                call.respond(communities)
//        }
        get(CommunityPaths.COMMUNITY) {
            val data= call.receive<RequestCommunity>()
            val community = CommunityDAO.read(id = data.communityID)
            if(community == null)
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(community)
        }
        post(CommunityPaths.COMMUNITY) {
            val data = call.receive<RequestCommunityCreation>()
            call.respond(CommunityFactoryService.create(data, CommunityDAO, UserDAO))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<RequestApproval>()
            call.respond(CommunityApprovalService.approveGroup(data, CommunityMemberDAO, GroupDAO))
        }
    }
}