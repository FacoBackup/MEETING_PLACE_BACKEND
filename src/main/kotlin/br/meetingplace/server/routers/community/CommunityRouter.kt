package br.meetingplace.server.routers.community

import br.meetingplace.server.db.mapper.group.GroupMapper
import br.meetingplace.server.modules.communityTODOTRANSACTIONS.dao.factory.CommunityFactory
import br.meetingplace.server.modules.communityTODOTRANSACTIONS.dao.moderators.Moderator
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
            call.respond(CommunityFactory.create(data))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<Approval>()
            call.respond(Moderator.approveGroup(data, groupMapper = GroupMapper))
        }
    }
}