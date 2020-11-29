package br.meetingplace.server.routers.search

import io.ktor.routing.*

fun Route.searchRouter() {
    route("/api") {
        get(SearchPaths.GROUP) {
            TODO("NOT YET IMPLEMENTED")
//            val data = call.receive<SimpleOperator>()
//            val group = GroupSearch.getClass().seeGroup(data, rwUser = UserDB.getClass(), rwGroup = GroupDB.getClass())
//
//            if (group == null)
//                call.respond(Status(404, StatusMessages.NOT_FOUND))
//            else
//                call.respond(group)

        }
        get(SearchPaths.COMMUNITY) {
            TODO("NOT YET IMPLEMENTED")
//            val data = call.receive<SimpleOperator>()
//            val search = CommunityDB.getClass().select(data.identifier.ID)
//
//            if (search == null)
//                call.respond(Status(404, StatusMessages.NOT_FOUND))
//            else
//                call.respond(search)
        }
        get(SearchPaths.USER) {
            TODO("NOT YET IMPLEMENTED")
//            val data = call.receive<SimpleOperator>()
//            val search = UserSearch.getClass().searchUser(data, rwUser = UserDB.getClass())
//
//            if (search == null)
//                call.respond(Status(404, StatusMessages.NOT_FOUND))
//            else
//                call.respond(search)
        }
    }
}