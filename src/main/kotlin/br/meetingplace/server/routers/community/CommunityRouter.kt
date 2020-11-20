package br.meetingplace.server.routers.community

import br.meetingplace.server.db.community.file.CommunityRW
import br.meetingplace.server.db.group.file.GroupRW
import br.meetingplace.server.db.topic.file.TopicRW
import br.meetingplace.server.db.user.file.UserRW
import br.meetingplace.server.modules.community.dao.factory.CommunityFactory
import br.meetingplace.server.modules.community.dao.moderators.Moderator
import br.meetingplace.server.routers.community.paths.CommunityPaths
import br.meetingplace.server.requests.community.Approval
import br.meetingplace.server.requests.generic.data.CreationData
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.communityRouter (){
    route("/api"){
        post(CommunityPaths.COMMUNITY) {
            val data = call.receive<CreationData>()
            call.respond(CommunityFactory.getClass().create(data, userDB = UserRW.getClass(), communityDB = CommunityRW.getClass()))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<Approval>()
            call.respond(Moderator.getClass().approveGroup(data, communityDB = CommunityRW.getClass(), userDB = UserRW.getClass(), groupDB = GroupRW.getClass()))
        }
        patch(CommunityPaths.TOPIC) {
            val data = call.receive<Approval>()
            call.respond(Moderator.getClass().approveTopic(data, communityDB = CommunityRW.getClass(), userDB = UserRW.getClass(), topicDB = TopicRW.getClass()))
        }
    }
}