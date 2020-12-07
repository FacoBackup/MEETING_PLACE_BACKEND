package br.meetingplace.server.routers.user

import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
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
        authenticate {
            get(UserPaths.USER+"/all") {
                val user = UserDAO.readAll()
                if (user.isEmpty())
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(user)
            }
            get(UserPaths.USER) {
                val data = call.receive<RequestUser>()
                val user = UserDAO.read(data.userID)
                if (user == null)
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(user)
            }
            get(UserPaths.USER+"/name") {
                val data = call.receive<RequestUser>()
                val user = UserDAO.readAllByAttribute(name = data.userID, null,null,null,null)
                if (user.isEmpty())
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(user)
            }

            delete(UserPaths.USER) {
                val data = call.receive<RequestUser>()
                call.respond(UserDeleteService.delete(data, UserDAO))
            }
            patch(UserPaths.PROFILE) {
                val data = call.receive<RequestProfileUpdate>()
                call.respond(UserUpdateService.updateProfile(data, UserDAO))
            }
            post<RequestSocial>(UserPaths.FOLLOW) {
                call.respond(UserSocialService.follow(it, UserSocialDAO, CommunityMemberDAO, CommunityDAO, UserDAO))
            }
            delete(UserPaths.UNFOLLOW) {
                val data = call.receive<RequestSocial>()
                call.respond(UserSocialService.unfollow(data, UserSocialDAO, CommunityMemberDAO))
            }
        }
    }
}