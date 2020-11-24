package br.meetingplace.server.routers.community

import br.meetingplace.server.db.community.CommunityDB
import br.meetingplace.server.db.group.GroupDB
import br.meetingplace.server.db.topic.TopicDB
import br.meetingplace.server.db.user.UserDB
import br.meetingplace.server.modules.community.dao.factory.CommunityFactory
import br.meetingplace.server.modules.community.dao.moderators.Moderator
import br.meetingplace.server.requests.community.Approval
import br.meetingplace.server.requests.generic.data.CreationData
import br.meetingplace.server.routers.community.paths.CommunityPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.communityRouter() {
    route("/api") {
        post(CommunityPaths.COMMUNITY) {
            val data = call.receive<CreationData>()
            call.respond(CommunityFactory.getClass().create(data, userDB = UserDB.getClass(), communityDB = CommunityDB.getClass()))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<Approval>()
            call.respond(Moderator.getClass().approveGroup(data, communityDB = CommunityDB.getClass(), userDB = UserDB.getClass(), groupDB = GroupDB.getClass()))
        }
        patch(CommunityPaths.TOPIC) {
            val data = call.receive<Approval>()
            call.respond(Moderator.getClass().approveTopic(data, communityDB = CommunityDB.getClass(), userDB = UserDB.getClass(), topicDB = TopicDB.getClass()))
        }
    }
}