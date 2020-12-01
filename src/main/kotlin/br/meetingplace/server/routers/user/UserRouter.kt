package br.meetingplace.server.routers.user

import br.meetingplace.server.modules.user.dao.UserMapper
import br.meetingplace.server.modules.user.service.delete.UserDeleteDAO
import br.meetingplace.server.modules.user.service.factory.UserFactoryDAO
import br.meetingplace.server.modules.user.service.profile.ProfileDAO
import br.meetingplace.server.modules.user.service.social.SocialDAO
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
                transaction { User.select { User.userName eq data.userID }.map { UserMapper.mapUser(it) }  }.firstOrNull()
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
            call.respond(UserFactoryDAO.create(user))
        }
        delete(UserPaths.USER) {
            val data = call.receive<LogDTO>()
            call.respond(UserDeleteDAO.delete(data))
        }
        patch(UserPaths.PROFILE) {
            val user = call.receive<ProfileUpdateDTO>()
            call.respond(ProfileDAO.updateProfile(user))
        }
        patch(UserPaths.FOLLOW) {
            val follow = call.receive<SubjectDTO>()
            call.respond(SocialDAO.follow(follow))
        }
        patch(UserPaths.UNFOLLOW) {
            val unfollow = call.receive<SubjectDTO>()
            call.respond(SocialDAO.unfollow(unfollow))
        }
    }
}