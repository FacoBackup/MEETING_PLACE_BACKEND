package br.meetingplace.server.routers.community

import br.meetingplace.server.db.mapper.community.CommunityMapper
import br.meetingplace.server.db.mapper.group.GroupMapper
import br.meetingplace.server.modules.community.dao.factory.CommunityFactory
import br.meetingplace.server.modules.community.dao.moderators.Moderator
import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.requests.community.Approval
import br.meetingplace.server.requests.generic.RequestCreationData
import br.meetingplace.server.requests.generic.SimpleOperator
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.communityRouter() {
    route("/api") {
//        get (CommunityPaths.MEMBER){
//            val data= call.receive<SimpleOperator>()
//            val communities = transaction { Community.select { Community.id eq data.subjectID }.map { CommunityMapper.mapCommunityDTO(it) } }
//            if(communities.isEmpty())
//                call.respond(Status(404, StatusMessages.NOT_FOUND))
//            else
//                call.respond(communities)
//        }
        get(CommunityPaths.COMMUNITY) {
            val data= call.receive<SimpleOperator>()
            val communities = transaction { Community.select { Community.name eq data.subjectID }.map { CommunityMapper.mapCommunityDTO(it) } }
            if(communities.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(communities)
        }
        post(CommunityPaths.COMMUNITY) {
            val data = call.receive<RequestCreationData>()
            call.respond(CommunityFactory.create(data))
        }
        patch(CommunityPaths.GROUP) {
            val data = call.receive<Approval>()
            call.respond(Moderator.approveGroup(data, groupMapper = GroupMapper))
        }
    }
}