package br.meetingplace.server.routers.user

import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.user.dao.UserDAO
import br.meetingplace.server.modules.user.services.delete.UserDeleteService
import br.meetingplace.server.modules.user.services.factory.UserFactoryService
import br.meetingplace.server.modules.user.services.profile.UserUpdateService
import br.meetingplace.server.modules.user.services.social.UserSocialService
import br.meetingplace.server.modules.user.entities.User
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.modules.group.dto.requests.RequestGroup
import br.meetingplace.server.modules.user.dao.social.UserSocialDAO
import br.meetingplace.server.modules.user.dto.requests.RequestProfileUpdate
import br.meetingplace.server.modules.user.dto.requests.RequestSocial
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException
import java.sql.SQLException

fun Route.userRouter() {

    route("/api") {
        get(UserPaths.USER) {

            val data = call.receive<RequestUser>()
            val user = UserDAO.read(data.userID)
            if (user == null)
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(user)
        }
        post(UserPaths.USER) {
            val data = call.receive<RequestUserCreation>()
            call.respond(UserFactoryService.create(data, UserDAO))
        }
        delete(UserPaths.USER) {
            val data = call.receive<RequestUser>()
            call.respond(UserDeleteService.delete(data, UserDAO))
        }
        patch(UserPaths.PROFILE) {
            val data = call.receive<RequestProfileUpdate>()
            call.respond(UserUpdateService.updateProfile(data, UserDAO))
        }
        post(UserPaths.FOLLOW) {
            val data = call.receive<RequestSocial>()
            call.respond(UserSocialService.follow(data, UserSocialDAO, CommunityMemberDAO, CommunityDAO, UserDAO))
        }
        delete(UserPaths.UNFOLLOW) {
            val data = call.receive<RequestSocial>()
            call.respond(UserSocialService.unfollow(data, UserSocialDAO, CommunityMemberDAO))
        }
    }
}