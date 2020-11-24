package br.meetingplace.server.routers.chat

import br.meetingplace.server.db.chat.ChatDB
import br.meetingplace.server.db.community.CommunityDB
import br.meetingplace.server.db.group.GroupDB
import br.meetingplace.server.db.user.UserDB
import br.meetingplace.server.modules.chat.dao.delete.DeleteMessage
import br.meetingplace.server.modules.chat.dao.dislike.DislikeMessage
import br.meetingplace.server.modules.chat.dao.favorite.FavoriteMessage
import br.meetingplace.server.modules.chat.dao.quote.QuoteMessage
import br.meetingplace.server.modules.chat.dao.send.SendMessage
import br.meetingplace.server.modules.chat.dao.share.ShareMessage
import br.meetingplace.server.modules.search.dao.ChatSearch
import br.meetingplace.server.requests.chat.data.MessageData
import br.meetingplace.server.requests.chat.operators.ChatComplexOperator
import br.meetingplace.server.requests.chat.operators.ChatFinderOperator
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator
import br.meetingplace.server.routers.chat.paths.ChatPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.chatRouter() {

    route("/api") {

        get(ChatPaths.CHAT) {
            val data = call.receive<ChatFinderOperator>()
            val chat = ChatSearch.getClass().seeChat(data, rwCommunity = CommunityDB.getClass(), rwGroup = GroupDB.getClass(), rwUser = UserDB.getClass(), rwChat = ChatDB.getClass())
            if (chat == null || chat.getID().isBlank()) {
                call.respond("Nothing found.")
            } else
                call.respond(chat)
        }
        post(ChatPaths.CHAT) {
            val data = call.receive<MessageData>()
            call.respond(SendMessage.getClass().sendMessage(data, rwChat = ChatDB.getClass(), rwCommunity = CommunityDB.getClass(), rwGroup = GroupDB.getClass(), userDB = UserDB.getClass()))
        }
        delete(ChatPaths.CHAT) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(DeleteMessage.getClass().deleteMessage(data, rwChat = ChatDB.getClass(), rwCommunity = CommunityDB.getClass(), rwGroup = GroupDB.getClass(), rwUser = UserDB.getClass()))
        }

        post(ChatPaths.QUOTE) {
            val data = call.receive<ChatComplexOperator>()
            call.respond(QuoteMessage.getClass().quoteMessage(data, rwUser = UserDB.getClass(), rwGroup = GroupDB.getClass(), rwCommunity = CommunityDB.getClass(), rwChat = ChatDB.getClass()))
        }

        patch(ChatPaths.LIKE) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(FavoriteMessage.getClass().favoriteMessage(data, rwUser = UserDB.getClass(), rwGroup = GroupDB.getClass(), rwCommunity = CommunityDB.getClass(), rwChat = ChatDB.getClass()))
        }
        patch(ChatPaths.DISLIKE) {
            val data = call.receive<ChatSimpleOperator>()
            call.respond(DislikeMessage.getClass().dislikeMessage(data, rwUser = UserDB.getClass(), rwGroup = GroupDB.getClass(), rwCommunity = CommunityDB.getClass(), rwChat = ChatDB.getClass()))
        }
        patch(ChatPaths.SHARE) {
            val data = call.receive<ChatComplexOperator>()
            call.respond(ShareMessage.getClass().shareMessage(data, userDB = UserDB.getClass(), groupDB = GroupDB.getClass(), communityDB = CommunityDB.getClass(), chatDB = ChatDB.getClass()))
        }
    }
}