package br.meetingplace.server.routers.search

import br.meetingplace.server.db.file.community.CommunityRW
import br.meetingplace.server.db.file.group.GroupRW
import br.meetingplace.server.db.file.user.UserRW
import br.meetingplace.server.modules.groups.dao.search.GroupSearch
import br.meetingplace.server.modules.user.dao.search.UserSearch
import br.meetingplace.server.routers.generic.requests.SimpleOperator
import br.meetingplace.server.routers.search.paths.SearchPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.SearchRouter (){
    route("/api"){
        get(SearchPaths.GROUP) {
            val data = call.receive<SimpleOperator>()
            val group = GroupSearch.getClass().seeGroup(data, rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass())

            if (group == null || group.getChatID().isBlank())
                call.respond("Nothing found.")
            else
                call.respond(group)

        }
        get(SearchPaths.COMMUNITY) {
            val data = call.receive<SimpleOperator>()
            val search = CommunityRW.getClass().select(data.identifier.ID)

            if (search == null)
                call.respond("Nothing found.")
            else
                call.respond(search)
        }
        get(SearchPaths.USER) {
            val data = call.receive<SimpleOperator>()
            val search = UserSearch.getClass().searchUser(data, rwUser = UserRW.getClass())

            if (search.isEmpty())
                call.respond("Nothing found.")
            else
                call.respond(search)
        }
    }
}