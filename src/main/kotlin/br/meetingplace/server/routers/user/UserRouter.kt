package br.meetingplace.server.routers.user

import br.meetingplace.server.db.mapper.user.UserMapper
import br.meetingplace.server.modules.user.dao.delete.UserDeleteDAO
import br.meetingplace.server.modules.user.dao.factory.UserFactoryDAO
import br.meetingplace.server.modules.user.dao.profile.ProfileDAO
import br.meetingplace.server.modules.user.dao.social.SocialDAO
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.RequestSimple
import br.meetingplace.server.requests.generic.SimpleOperator
import br.meetingplace.server.requests.users.data.ProfileData
import br.meetingplace.server.requests.users.data.UserCreationData
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
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

            val data = call.receive<RequestSimple>()
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
            val user = call.receive<UserCreationData>()
            call.respond(UserFactoryDAO.create(user))
        }
        delete(UserPaths.USER) {
            val data = call.receive<RequestSimple>()
            call.respond(UserDeleteDAO.delete(data))
        }
        patch(UserPaths.PROFILE) {
            val user = call.receive<ProfileData>()
            call.respond(ProfileDAO.updateProfile(user))
        }
        patch(UserPaths.FOLLOW) {
            val follow = call.receive<SimpleOperator>()
            call.respond(SocialDAO.follow(follow))
        }
        patch(UserPaths.UNFOLLOW) {
            val unfollow = call.receive<SimpleOperator>()
            call.respond(SocialDAO.unfollow(unfollow))
        }
    }
}