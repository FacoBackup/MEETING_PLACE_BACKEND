package br.meetingplace.server.routers.community

import br.meetingplace.server.db.file.community.CommunityRW
import br.meetingplace.server.db.file.group.GroupRW
import br.meetingplace.server.db.file.topic.TopicRW
import br.meetingplace.server.db.file.user.UserRW
import br.meetingplace.server.modules.community.dao.factory.CommunityFactory
import br.meetingplace.server.modules.community.dao.moderators.Moderator
import br.meetingplace.server.routers.community.paths.CommunityPaths
import br.meetingplace.server.routers.community.requests.ApprovalData
import br.meetingplace.server.routers.generic.requests.CreationData
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.CommunityRouter (){
    route("/api"){
        post(CommunityPaths.COMMUNITY) {
            val data = call.receive<CreationData>()
            call.respond(CommunityFactory.getClass().create(data, rwUser = UserRW.getClass(), rwCommunity = CommunityRW.getClass()))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<ApprovalData>()
            call.respond(Moderator.getClass().approveGroup(data, rwCommunity = CommunityRW.getClass(), rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass()))
        }
        patch(CommunityPaths.TOPIC) {
            val data = call.receive<ApprovalData>()
            call.respond(Moderator.getClass().approveTopic(data, rwCommunity = CommunityRW.getClass(), rwUser = UserRW.getClass(), rwTopic = TopicRW.getClass()))
        }
    }
}