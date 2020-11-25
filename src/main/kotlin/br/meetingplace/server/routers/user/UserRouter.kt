package br.meetingplace.server.routers.user

import br.meetingplace.server.db.mapper.user.UserMapper
import br.meetingplace.server.modules.user.dao.delete.UserDelete
import br.meetingplace.server.modules.user.dao.factory.UserFactoryDAO
import br.meetingplace.server.modules.user.dao.profile.ProfileDAO
import br.meetingplace.server.modules.user.dao.social.SocialDAO
import br.meetingplace.server.requests.generic.data.Login
import br.meetingplace.server.requests.generic.operators.SimpleOperator
import br.meetingplace.server.requests.users.data.ProfileData
import br.meetingplace.server.requests.users.data.UserCreationData
import br.meetingplace.server.routers.user.paths.UserPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.userRouter() {

    route("/api") {
        get(UserPaths.USER) {
            TODO("NOT YET IMPLEMENTED")
//            val data = call.receive<Login>()
//            val user = UserDB.select(data.email)
//
//            if (user == null)
//                call.respond(Status(404, StatusMessages.NOT_FOUND))
//            else
//                call.respond(user)
        }
        post(UserPaths.USER) {
            val user = call.receive<UserCreationData>()
            call.respond(UserFactoryDAO.create(user))
        }
        delete(UserPaths.USER) {
            val data = call.receive<Login>()
            call.respond(UserDelete.delete(data))
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