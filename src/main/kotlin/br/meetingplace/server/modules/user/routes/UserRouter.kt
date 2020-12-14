package br.meetingplace.server.modules.user.routes

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.message.dao.MessageDAO
import br.meetingplace.server.modules.message.dao.opinions.MessageOpinionDAO
import br.meetingplace.server.modules.message.services.opinion.MessageOpinionService
import br.meetingplace.server.modules.user.dao.social.UserSocialDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dto.requests.RequestProfileUpdate
import br.meetingplace.server.modules.user.dto.requests.RequestSocial
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.services.delete.UserDeleteService
import br.meetingplace.server.modules.user.services.factory.UserFactoryService
import br.meetingplace.server.modules.user.services.profile.UserUpdateService
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
        patch(UserPaths.USER + "/search") {
            val data = call.receive<RequestUser>()
            val user = UserDAO.read(data.userID)
            println("Get user profile REQUESTED")
            println("data: $data")
            if (user == null)
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(user)
        }

        authenticate {
            patch("/follower"){
                val data = call.receive<RequestUser>()
                val log = call.log
                println(data.userID)
                if(log != null)
                    call.respond(UserSocialDAO.check(followedID = data.userID, userID = log.userID))

                else call.respond(false)
            }
            get(UserPaths.USER +"/all") {
                val user = UserDAO.readAll()
                if (user.isEmpty())
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(user)
            }
            get(UserPaths.USER) {
                val log = call.log
                if(log != null){
                    val user = UserDAO.read(log.userID)
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
            patch(UserPaths.PROFILE) {
                val data = call.receive<RequestProfileUpdate>()
                val log = call.log
                if(log != null)
                    call.respond(UserUpdateService.updateProfile(requester = log.userID,data, UserDAO))
                else call.respond(HttpStatusCode.Unauthorized)

            }
            get("/followers"){
                val log = call.log
                if(log != null)
                    call.respond(UserSocialDAO.readAll(userID = log.userID, following = false))
                else call.respond(HttpStatusCode.Unauthorized)
            }
            get("/following"){
                val log = call.log
                if(log != null)
                    call.respond(UserSocialDAO.readAll(userID = log.userID, following = true))
                else call.respond(HttpStatusCode.Unauthorized)
            }
            post<RequestSocial>(UserPaths.FOLLOW) {
                val log = call.log
                println("FOLLOW requested")

                if(log != null){
                    println(UserSocialDAO.readAll(userID = log.userID, following = true))
                    call.respond(UserSocialService.follow(requester = log.userID,it, UserSocialDAO, CommunityMemberDAO, CommunityDAO, UserDAO))
                }

                else call.respond(HttpStatusCode.Unauthorized)
            }
            delete(UserPaths.UNFOLLOW) {
                val data = call.receive<RequestSocial>()
                val log = call.log
                println("UNFOLLOW requested")
                if(log != null)
                    call.respond(UserSocialService.unfollow(requester = log.userID,data, UserSocialDAO, CommunityMemberDAO))
                else call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}