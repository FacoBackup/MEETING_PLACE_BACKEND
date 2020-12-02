package br.meetingplace.server.routers.user

import br.meetingplace.server.modules.user.dao.UserDAO
import br.meetingplace.server.modules.user.service.delete.UserDeleteService
import br.meetingplace.server.modules.user.service.factory.UserFactoryService
import br.meetingplace.server.modules.user.service.profile.UserUpdateService
import br.meetingplace.server.modules.user.service.social.UserSocialService
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.generic.LogDTO
import br.meetingplace.server.request.dto.generic.SubjectDTO
import br.meetingplace.server.request.dto.users.ProfileUpdateDTO
import br.meetingplace.server.request.dto.users.UserCreationDTO
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

            val data = call.receive<LogDTO>()
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
            val user = call.receive<UserCreationDTO>()
            call.respond(UserFactoryService.create(user))
        }
        delete(UserPaths.USER) {
            val data = call.receive<LogDTO>()
            call.respond(UserDeleteService.delete(data))
        }
        patch(UserPaths.PROFILE) {
            val user = call.receive<ProfileUpdateDTO>()
            call.respond(UserUpdateService.updateProfile(user))
        }
        patch(UserPaths.FOLLOW) {
            val follow = call.receive<SubjectDTO>()
            call.respond(UserSocialService.follow(follow))
        }
        patch(UserPaths.UNFOLLOW) {
            val unfollow = call.receive<SubjectDTO>()
            call.respond(UserSocialService.unfollow(unfollow))
        }
    }
}