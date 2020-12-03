package br.meetingplace.server.routers.community

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.community.services.factory.CommunityFactoryService
import br.meetingplace.server.modules.community.services.approval.CommunityApprovalService
import br.meetingplace.server.modules.community.entities.Community
import br.meetingplace.server.modules.community.dto.requests.RequestApproval
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.group.dto.requests.RequestGroup
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

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
            val data= call.receive<RequestGroup>()
            val communities = transaction { Community.select { Community.name eq data.subjectID }.map { CommunityDAO.mapCommunityDTO(it) } }
            if(communities.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(communities)
        }
        post(CommunityPaths.COMMUNITY) {
            val data = call.receive<RequestCommunityCreation>()
            call.respond(CommunityFactoryService.create(data))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<RequestApproval>()
            call.respond(CommunityApprovalService.approveGroup(data, groupMapper = GroupDAO))
        }
    }
}