package br.meetingplace.server.modules.community.routes

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.community.dto.requests.RequestCommunity
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.services.factory.CommunityFactoryService
import br.meetingplace.server.modules.community.services.read.CommunityReadService
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.communityRouter() {
    route("/api") {
        patch("/get/community/by/id"){
            val data = call.receive<RequestCommunity>()
            val log = call.log
            if(log != null){
                val response = CommunityReadService.readCommunityByID(
                    requester = log.userID,
                    communityID = data.communityID,
                    communityDAO = CommunityDAO,
                    communityMemberDAO = CommunityMemberDAO)
                if(response != null){


                    call.respond(response)
                }

                else
                    call.respond(HttpStatusCode.NoContent)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
//        patch("/get/community/related/communities"){
//            val data = call.receive<RequestCommunity>()
//            val log = call.log
//            if(log != null)
//                call.respond(CommunityReadService.readCommunitiesRelatedToCommunity(
//                    communityMemberDAO = CommunityMemberDAO,
//                    communityID = data.communityID,
//                    userDAO = UserDAO,
//                    communityDAO = CommunityDAO
//                ))
//            else call.respond(HttpStatusCode.Unauthorized)
//
//        }
        get ("/communities/related"){
            val log = call.log
            if(log != null)
                call.respond(CommunityReadService.readAllUserCommunities(requester = log.userID, communityDAO = CommunityDAO, communityMemberDAO = CommunityMemberDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/get/community/related/users"){
            val data = call.receive<RequestCommunity>()
            val log = call.log
            if(log != null)
                call.respond(CommunityReadService.readUsersRelatedToCommunity(
                    communityMemberDAO = CommunityMemberDAO,
                    communityID = data.communityID,
                    userDAO = UserDAO,
                    communityDAO = CommunityDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        patch("/search/community") {
            val data= call.receive<RequestCommunity>()
            val log = call.log
            if(log != null){
                if(data.communityID.isNotBlank())
                    call.respond(CommunityReadService.readCommunityByName(requester = log.userID, name= data.communityID, communityDAO = CommunityDAO, communityMemberDAO = CommunityMemberDAO))
            }

            else call.respond(HttpStatusCode.Unauthorized)
        }

        post<RequestCommunityCreation>("/community") {
            val log = call.log
            if(log != null)
                call.respond(CommunityFactoryService.create(requester = log.userID,it, CommunityDAO, communityMemberDAO = CommunityMemberDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
    }
}