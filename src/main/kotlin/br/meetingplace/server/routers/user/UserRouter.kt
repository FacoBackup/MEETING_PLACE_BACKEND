package br.meetingplace.server.routers.user

import br.meetingplace.server.modules.user.dao.UserDAO
import br.meetingplace.server.modules.user.services.delete.UserDeleteService
import br.meetingplace.server.modules.user.services.factory.UserFactoryService
import br.meetingplace.server.modules.user.services.profile.UserUpdateService
import br.meetingplace.server.modules.user.services.social.UserSocialService
import br.meetingplace.server.modules.user.entities.User
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.modules.group.dto.requests.RequestGroup
import br.meetingplace.server.modules.user.dto.requests.RequestProfileUpdate
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
            val user = try {
                transaction { User.select { User.userName eq data.userID }.map { UserDAO.mapUser(it) }  }.firstOrNull()
            }catch(sql: SQLException){
                null
            }catch (psql: PSQLException){
                null
            }

            if (user == null)
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(user)
        }
        post(UserPaths.USER) {
            val user = call.receive<RequestUserCreation>()
            call.respond(UserFactoryService.create(user))
        }
        delete(UserPaths.USER) {
            val data = call.receive<RequestUser>()
            call.respond(UserDeleteService.delete(data))
        }
        patch(UserPaths.PROFILE) {
            val user = call.receive<RequestProfileUpdate>()
            call.respond(UserUpdateService.updateProfile(user))
        }
        patch(UserPaths.FOLLOW) {
            val follow = call.receive<RequestGroup>()
            call.respond(UserSocialService.follow(follow))
        }
        patch(UserPaths.UNFOLLOW) {
            val unfollow = call.receive<RequestGroup>()
            call.respond(UserSocialService.unfollow(unfollow))
        }
    }
}