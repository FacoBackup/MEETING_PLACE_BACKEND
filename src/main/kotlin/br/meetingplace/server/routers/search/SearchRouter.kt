package br.meetingplace.server.routers.search

import br.meetingplace.server.db.community.CommunityDB
import br.meetingplace.server.db.group.GroupDB
import br.meetingplace.server.db.user.UserDB
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.search.dao.GroupSearch
import br.meetingplace.server.modules.search.dao.UserSearch
import br.meetingplace.server.requests.generic.operators.SimpleOperator
import br.meetingplace.server.routers.search.paths.SearchPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.searchRouter() {
    route("/api") {
        get(SearchPaths.GROUP) {
            val data = call.receive<SimpleOperator>()
            val group = GroupSearch.getClass().seeGroup(data, rwUser = UserDB.getClass(), rwGroup = GroupDB.getClass())

            if (group == null)
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(group)

        }
        get(SearchPaths.COMMUNITY) {
            val data = call.receive<SimpleOperator>()
            val search = CommunityDB.getClass().select(data.identifier.ID)

            if (search == null)
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(search)
        }
        get(SearchPaths.USER) {
            val data = call.receive<SimpleOperator>()
            val search = UserSearch.getClass().searchUser(data, rwUser = UserDB.getClass())

            if (search == null)
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(search)
        }
    }
}