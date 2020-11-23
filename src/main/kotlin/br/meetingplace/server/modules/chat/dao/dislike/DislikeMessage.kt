package br.meetingplace.server.modules.chat.dao.dislike

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator

class DislikeMessage private constructor() {
    companion object {
        private val Class = DislikeMessage()
        fun getClass() = Class
    }

    fun dislikeMessage(data: ChatSimpleOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface, rwCommunity: CommunityDBInterface, rwChat: ChatDBInterface) {
        val user = rwUser.select(data.login.email)
        lateinit var liked: List<String>

        if (user != null) {
            when (data.receiver.userGroup || data.receiver.communityGroup) {
                true -> { //GROUP
                    val group = rwGroup.select(data.receiver.receiverID)
                    if (group != null) {
                        val chat = rwChat.select(group.getChatID())
                        when (data.receiver.communityGroup) {
                            true -> {
                                val community = rwCommunity.select(group.getOwner().ID)
                                if (community != null && chat != null && group.getID() in community.getGroups()) {
                                    liked = chat.getLiked()
                                    liked.remove(data.messageID)
                                    chat.setLiked(liked = liked)
                                    rwChat.insert(chat)
                                }
                            }
                            false -> {
                                if (chat != null) {
                                    liked = chat.getLiked()
                                    liked.remove(data.messageID)
                                    chat.setLiked(liked = liked)
                                    rwChat.insert(chat)
                                }
                            }
                        }
                    }
                }
                false -> { //USER <-> USER
                    val chat = rwChat.select(data.receiver.chatID)
                    if (chat != null) {
                        liked = chat.getLiked()
                        liked.remove(data.messageID)
                        chat.setLiked(liked = liked)
                        rwChat.insert(chat)
                    }
                }
            }
        }
    }
}