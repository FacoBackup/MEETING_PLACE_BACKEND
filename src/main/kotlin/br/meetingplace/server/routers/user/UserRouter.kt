package br.meetingplace.server.routers.user

import br.meetingplace.server.db.community.file.CommunityRW
import br.meetingplace.server.db.group.file.GroupRW
import br.meetingplace.server.db.topic.file.TopicRW
import br.meetingplace.server.db.user.file.UserRW
import br.meetingplace.server.modules.members.dao.remove.RemoveMember
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
            val user = UserRW.getClass().select(data.email)

            if (user == null)
                call.respond("Nothing found.")
            else
                call.respond(user)
        }
        post(UserPaths.USER) {
            val user = call.receive<UserCreationData>()
            when (UserFactory.getClass().create(user, rwUser = UserRW.getClass())) {
                true -> {
                    call.respond("Created successfully.")
                }
                false -> {
                    call.respond("Something went wrong.")
                }
            }
        }
        delete(UserPaths.USER) {
            val data = call.receive<Login>()
            call.respond(UserDelete.getClass().delete(data, userDB = UserRW.getClass(), topicDB = TopicRW.getClass(), groupDB = GroupRW.getClass(), communityDB = CommunityRW.getClass()))
        }

        patch(UserPaths.NOTIFICATIONS) {
            val data = call.receive<Login>()
            call.respond(Profile.getClass().clearNotifications(data, userDB = UserRW.getClass()))
        }

        patch(UserPaths.PROFILE) {
            val user = call.receive<ProfileData>()
            call.respond(Profile.getClass().updateProfile(user, userDB = UserRW.getClass()))
        }
        patch(UserPaths.FOLLOW) {
            val follow = call.receive<SimpleOperator>()
            call.respond(Social.getClass().follow(follow, userDB = UserRW.getClass()))
        }
        patch(UserPaths.UNFOLLOW) {
            val unfollow = call.receive<SimpleOperator>()
            call.respond(Social.getClass().unfollow(unfollow, userDB = UserRW.getClass()))
        }
    }
}