package br.meetingplace.server.modules.user.routes

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.topic.dao.topic.TopicDAO
import br.meetingplace.server.modules.user.dao.social.UserSocialDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dto.requests.RequestProfileUpdate
import br.meetingplace.server.modules.user.dto.requests.RequestSocial
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.dto.response.UserSearchDTO
import br.meetingplace.server.modules.user.services.delete.UserDeleteService
import br.meetingplace.server.modules.user.services.factory.UserFactoryService
import br.meetingplace.server.modules.user.services.profile.UserUpdateService
import br.meetingplace.server.modules.user.services.read.UserReadService
import br.meetingplace.server.modules.user.services.search.UserSearchService
import br.meetingplace.server.modules.user.services.social.UserSocialService
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.userRouter() {

    route("/api") {
        post<RequestUserCreation>(UserPaths.USER) {
            call.respond(UserFactoryService.create(it, UserDAO))
        }
        patch("/get/profile") {
            val data = call.receive<RequestUser>()
            val log = call.log
            if(log != null){
                val response = UserReadService.read(data.userID, topicDAO = TopicDAO, userDAO = UserDAO, userSocialDAO = UserSocialDAO)
                if(response == null)
                    call.respond(HttpStatusCode.NoContent)
                else
                    call.respond(response)

            }

            else call.respond(HttpStatusCode.InternalServerError)


        }

        get(UserPaths.USER +"/all") {
            val user = UserDAO.readAll()
            if (user.isEmpty())
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(user)
        }
        authenticate {
            patch("/verify/follower"){
                val data = call.receive<RequestUser>()
                val log = call.log
                if(log != null)
                    call.respond(UserSocialDAO.check(followedID = log.userID, userID = data.userID))
                else call.respond(false)
            }
            patch("/verify/following"){
                val data = call.receive<RequestUser>()
                val log = call.log
                if(log != null)
                    call.respond(UserSocialDAO.check(followedID = data.userID, userID = log.userID))
                else call.respond(false)
            }
            patch("/get/followers"){
                val log = call.log
                val data = call.receive<RequestUser>()
                if(log != null)
                    call.respond(UserSocialService.readFollowers(data.userID, UserSocialDAO, UserDAO))
                else call.respond(HttpStatusCode.Unauthorized)
            }
            patch("/get/following"){
                val log = call.log
                val data = call.receive<RequestUser>()
                if(log != null)
                    call.respond(UserSocialService.readFollowing(data.userID, UserSocialDAO, UserDAO))
                else call.respond(HttpStatusCode.Unauthorized)
            }
            patch("/get/simplified/user/profile"){
                val data = call.receive<RequestUser>()
                val log = call.log
                if(log != null){
                    val user = UserDAO.readByID(data.userID)
                    if(user != null)
                        call.respond(UserSearchDTO(name= user.name,email = user.email,imageURL = user.imageURL,isFollowing = false))
                    else
                        call.respond(HttpStatusCode.NoContent)
                }

                else call.respond(HttpStatusCode.Unauthorized)
            }
            patch("/search/user"){
                val data = call.receive<RequestUser>()
                val log = call.log
                if(log != null)
                    call.respond(UserSearchService.searchUser(requester = log.userID, userID = data.userID, UserDAO, UserSocialDAO))
                else call.respond(HttpStatusCode.Unauthorized)
            }
            get(UserPaths.USER) {
                val log = call.log
                if(log != null){
                    val user = UserDAO.readByID(log.userID)
                    if (user == null)
                        call.respond(HttpStatusCode.NotFound)
                    else
                        call.respond(user)
                }
                else call.respond(HttpStatusCode.Unauthorized)
            }
            get(UserPaths.USER +"/name") {
                val data = call.receive<RequestUser>()
                val user = UserDAO.readAllByAttribute(name = data.userID, null,null,null,null)
                if (user.isEmpty())
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(user)
            }

            delete(UserPaths.USER) {
                val data = call.receive<RequestUser>()
                val log = call.log
                if(log != null)
                    call.respond(UserDeleteService.delete(requester = log.userID,data, UserDAO))
                else call.respond(HttpStatusCode.Unauthorized)

            }
            put("/profile") {
                val data = call.receive<RequestProfileUpdate>()
                val log = call.log
                if(log != null)
                    call.respond(UserUpdateService.updateProfile(requester = log.userID,data, UserDAO))
                else call.respond(HttpStatusCode.Unauthorized)

            }

            post<RequestSocial>(UserPaths.FOLLOW) {
                val log = call.log

                if(log != null){

                    call.respond(UserSocialService.follow(requester = log.userID,it, UserSocialDAO, CommunityMemberDAO, CommunityDAO, UserDAO))
                }

                else call.respond(HttpStatusCode.Unauthorized)
            }
            delete(UserPaths.UNFOLLOW) {
                val data = call.receive<RequestSocial>()
                val log = call.log

                if(log != null)
                    call.respond(UserSocialService.unfollow(requester = log.userID,data, UserSocialDAO, CommunityMemberDAO))
                else call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}