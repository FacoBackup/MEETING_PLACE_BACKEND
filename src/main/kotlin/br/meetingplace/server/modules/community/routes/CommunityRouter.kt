package br.meetingplace.server.modules.community.routes

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.community.dto.requests.CommunityUpdateDTO
import br.meetingplace.server.modules.community.dto.requests.RequestCommunity
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityMember
import br.meetingplace.server.modules.community.services.factory.CommunityFactoryService
import br.meetingplace.server.modules.community.services.member.CommunityMemberService
import br.meetingplace.server.modules.community.services.read.CommunityReadService
import br.meetingplace.server.modules.topic.dao.topic.TopicDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.communityRouter() {
    route("/api") {
        put("/lower/member"){
            val data = call.receive<RequestCommunityMember>()
            val log = call.log
            if(log != null)
                call.respond(CommunityMemberService.lowerMember(requester = log.userID, data = data,communityMemberDAO = CommunityMemberDAO))

            else call.respond(HttpStatusCode.Unauthorized)
        }
        put("/promote/member"){
            val data = call.receive<RequestCommunityMember>()
            val log = call.log
            if(log != null)
                call.respond(CommunityMemberService.promoteMember(requester = log.userID, data = data,communityMemberDAO = CommunityMemberDAO))

            else call.respond(HttpStatusCode.Unauthorized)
        }
        delete("/remove/member"){
            val data = call.receive<RequestCommunityMember>()
            val log = call.log
            if(log != null)
                call.respond(CommunityMemberService.removeMember(requester = log.userID, data = data,communityMemberDAO = CommunityMemberDAO))

            else call.respond(HttpStatusCode.Unauthorized)
        }
        put("/update/community"){
            val data = call.receive<CommunityUpdateDTO>()
            val log = call.log
            if(log != null)
                call.respond(CommunityDAO.update(communityID = data.communityID, about = data.about, imageURL = data.imageURL, backgroundImageURL = data.backgroundImageURL))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        patch("/get/members"){
            val data = call.receive<RequestCommunity>()
            val log = call.log
            if(log != null){
                call.respond(CommunityReadService.readMembers(
                    userDAO=UserDAO,
                    communityID = data.communityID,
                    communityDAO = CommunityDAO,
                    communityMemberDAO = CommunityMemberDAO))

            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/get/mods"){
            val data = call.receive<RequestCommunity>()
            val log = call.log
            if(log != null){
                call.respond(CommunityReadService.readMods(
                    userDAO=UserDAO,
                    communityID = data.communityID,
                    communityDAO = CommunityDAO,
                    communityMemberDAO = CommunityMemberDAO))

            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/get/followers/community"){
            val data = call.receive<RequestCommunity>()
            val log = call.log
            if(log != null){
                call.respond(CommunityReadService.readFollowers(
                    userDAO=UserDAO,
                    communityID = data.communityID,
                    communityDAO = CommunityDAO,
                    communityMemberDAO = CommunityMemberDAO))

            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/get/community/by/id"){
            val data = call.receive<RequestCommunity>()
            val log = call.log
            if(log != null){
                val response = CommunityReadService.readCommunityByID(
                    requester = log.userID,
                    communityID = data.communityID,
                    communityDAO = CommunityDAO,
                    communityMemberDAO = CommunityMemberDAO,
                    topicDAO = TopicDAO
                    )
                if(response != null)
                    call.respond(response)
                else
                    call.respond(HttpStatusCode.NoContent)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/get/all/related/communities"){
            val data = call.receive<RequestCommunity>()
            val log = call.log
            if(log != null)
                call.respond(CommunityReadService.readAllRelatedCommunities(
                    communityMemberDAO = CommunityMemberDAO,
                    communityID = data.communityID,
                    communityDAO = CommunityDAO,
                    requester = log.userID
                ))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        patch ("/get/all/user/communities"){
            val data = call.receive<RequestUser>()
            val log = call.log
            if(log != null)
                call.respond(CommunityReadService.readAllUserCommunities(userID = data.userID, communityDAO = CommunityDAO, communityMemberDAO = CommunityMemberDAO))
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