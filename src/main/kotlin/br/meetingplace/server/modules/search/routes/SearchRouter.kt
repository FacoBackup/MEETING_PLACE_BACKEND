package br.meetingplace.server.modules.search.routes

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.community.dto.requests.RequestCommunity
import br.meetingplace.server.modules.community.services.read.CommunityReadService
import br.meetingplace.server.modules.search.dto.SearchDTO
import br.meetingplace.server.modules.search.service.SearchService
import br.meetingplace.server.modules.user.dao.social.UserSocialDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.searchRouter() {
    route("/api") {

        patch("/search/user"){
            val data = call.receive<SearchDTO>()
            val log = call.log
            if(log != null){
                call.respond(SearchService.searchUser(log.userID,maxID = data.maxID, userDAO = UserDAO, userName= data.subjectName, userSocialDAO = UserSocialDAO))
            }
            else call.respond(HttpStatusCode.InternalServerError)
        }
        patch("/search/community") {
            val data= call.receive<SearchDTO>()
            val log = call.log
            if(log != null){

                call.respond(
                    SearchService.searchCommunity(
                    requester = log.userID,
                    name= data.subjectName,
                    communityDAO = CommunityDAO,
                    communityMemberDAO = CommunityMemberDAO,
                        maxID = data.maxID
                    ))
            }

            else call.respond(HttpStatusCode.Unauthorized)
        }
    }
}
