package br.meetingplace.server.routers.chat

import br.meetingplace.server.db.file.chat.ChatRW
import br.meetingplace.server.db.file.community.CommunityRW
import br.meetingplace.server.db.file.group.GroupRW
import br.meetingplace.server.db.file.user.UserRW
import br.meetingplace.server.modules.chat.dao.delete.DeleteMessage
import br.meetingplace.server.modules.chat.dao.disfavor.DisfavorMessage
import br.meetingplace.server.modules.chat.dao.favorite.FavoriteMessage
import br.meetingplace.server.modules.chat.dao.quote.QuoteMessage
import br.meetingplace.server.modules.chat.dao.search.ChatSearch
import br.meetingplace.server.modules.chat.dao.send.SendMessage
import br.meetingplace.server.modules.chat.dao.share.ShareMessage
import br.meetingplace.server.routers.chat.paths.ChatPaths
import br.meetingplace.server.routers.chat.requests.ChatComplexOperator
import br.meetingplace.server.routers.chat.requests.ChatFinderOperator
import br.meetingplace.server.routers.chat.requests.ChatSimpleOperator
import br.meetingplace.server.routers.chat.requests.MessageData
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.ChatRouter (){

    route("/api"){

        get(ChatPaths.CHAT) {
            val data = call.receive<ChatFinderOperator>()
            val chat = ChatSearch.getClass().seeChat(data, rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass(),rwChat = ChatRW.getClass())
            if (chat == null || chat.getID().isBlank()) {
                call.respond("Nothing found.")
            } else
                call.respond(chat)
        }
        post(ChatPaths.CHAT) {
            val data = call.receive<MessageData>()
            call.respond(SendMessage.getClass().sendMessage(data, rwChat = ChatRW.getClass(),rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass()))
        }
        delete(ChatPaths.CHAT) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(DeleteMessage.getClass().deleteMessage(data, rwChat = ChatRW.getClass(),rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass()))
        }

        post(ChatPaths.QUOTE) {
            val data = call.receive<ChatComplexOperator>()
            call.respond(QuoteMessage.getClass().quoteMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
        }

        patch(ChatPaths.LIKE) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(FavoriteMessage.getClass().favoriteMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
        }
        patch(ChatPaths.DISLIKE) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(DisfavorMessage.getClass().disfavorMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
        }
        patch(ChatPaths.SHARE) {
            val data = call.receive<ChatComplexOperator>()
            call.respond(ShareMessage.getClass().shareMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
        }
    }
}