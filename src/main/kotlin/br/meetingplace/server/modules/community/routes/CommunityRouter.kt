package br.meetingplace.server.modules.community.routes

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dto.requests.RequestCommunity
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.services.factory.CommunityFactoryService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
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
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(community)
        }
        post<RequestCommunityCreation>(CommunityPaths.COMMUNITY) {
            val log = call.log
            if(log != null)
                call.respond(CommunityFactoryService.create(requester = log.userID,it, CommunityDAO, UserDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
    }
}