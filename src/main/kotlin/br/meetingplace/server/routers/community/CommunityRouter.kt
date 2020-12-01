package br.meetingplace.server.routers.community

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.group.dao.GroupDAO
import br.meetingplace.server.modules.community.service.factory.CommunityFactory
import br.meetingplace.server.modules.community.service.approval.GroupApproval
import br.meetingplace.server.modules.community.entitie.Community
import br.meetingplace.server.request.dto.community.ApprovalDTO
import br.meetingplace.server.request.dto.community.CommunityCreationDTO
import br.meetingplace.server.request.dto.generic.SubjectDTO
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
            val data= call.receive<SubjectDTO>()
            val communities = transaction { Community.select { Community.name eq data.subjectID }.map { CommunityDAO.mapCommunityDTO(it) } }
            if(communities.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(communities)
        }
        post(CommunityPaths.COMMUNITY) {
            val data = call.receive<CommunityCreationDTO>()
            call.respond(CommunityFactory.create(data))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<ApprovalDTO>()
            call.respond(GroupApproval.approveGroup(data, groupMapper = GroupDAO))
        }
    }
}