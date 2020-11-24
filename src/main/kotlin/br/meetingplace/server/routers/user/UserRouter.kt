package br.meetingplace.server.routers.user

import br.meetingplace.server.db.community.CommunityDB
import br.meetingplace.server.db.group.GroupDB
import br.meetingplace.server.db.topic.TopicDB
import br.meetingplace.server.db.user.UserDB
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.user.dao.delete.UserDelete
import br.meetingplace.server.modules.user.dao.factory.UserFactory
import br.meetingplace.server.modules.user.dao.profile.Profile
import br.meetingplace.server.modules.user.dao.social.Social
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
            val data = call.receive<Login>()
            val user = UserDB.getClass().select(data.email)

            if (user == null)
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(user)
        }
        post(UserPaths.USER) {
            val user = call.receive<UserCreationData>()
            call.respond(UserFactory.getClass().create(user, rwUser = UserDB.getClass()))
        }
        delete(UserPaths.USER) {
            val data = call.receive<Login>()
            call.respond(UserDelete.getClass().delete(data, userDB = UserDB.getClass(), topicDB = TopicDB.getClass(), groupDB = GroupDB.getClass(), communityDB = CommunityDB.getClass()))
        }

        patch(UserPaths.NOTIFICATIONS) {
            val data = call.receive<Login>()
            call.respond(Profile.getClass().clearNotifications(data, userDB = UserDB.getClass()))
        }

        patch(UserPaths.PROFILE) {
            val user = call.receive<ProfileData>()
            call.respond(Profile.getClass().updateProfile(user, userDB = UserDB.getClass()))
        }
        patch(UserPaths.FOLLOW) {
            val follow = call.receive<SimpleOperator>()
            call.respond(Social.getClass().follow(follow, userDB = UserDB.getClass()))
        }
        patch(UserPaths.UNFOLLOW) {
            val unfollow = call.receive<SimpleOperator>()
            call.respond(Social.getClass().unfollow(unfollow, userDB = UserDB.getClass()))
        }
    }
}