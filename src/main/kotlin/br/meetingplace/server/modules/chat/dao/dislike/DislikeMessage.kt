package br.meetingplace.server.modules.chat.dao.dislike

import br.meetingplace.server.requests.chat.operators.ChatSimpleOperator

object DislikeMessage {
    fun dislikeMessage(data: ChatSimpleOperator) {
        TODO("NOT YET IMPLEMENTED")
//        val user = rwUser.select(data.login.email)
//        lateinit var liked: List<String>
//
//        if (user != null) {
//            when (data.receiver.userGroup || data.receiver.communityGroup) {
//                true -> { //GROUP
//                    val group = rwGroup.select(data.receiver.receiverID)
//                    if (group != null) {
//                        val chat = rwChat.select(group.getChatID())
//                        when (data.receiver.communityGroup) {
//                            true -> {
//                                val community = rwCommunity.select(group.getOwner().ID)
//                                if (community != null && chat != null && group.getID() in community.getGroups()) {
//                                    liked = chat.getLiked()
//                                    liked.remove(data.messageID)
//                                    chat.setLiked(liked = liked)
//                                    rwChat.insert(chat)
//                                }
//                            }
//                            false -> {
//                                if (chat != null) {
//                                    liked = chat.getLiked()
//                                    liked.remove(data.messageID)
//                                    chat.setLiked(liked = liked)
//                                    rwChat.insert(chat)
//                                }
//                            }
//                        }
//                    }
//                }
//                false -> { //USER <-> USER
//                    val chat = rwChat.select(data.receiver.chatID)
//                    if (chat != null) {
//                        liked = chat.getLiked()
//                        liked.remove(data.messageID)
//                        chat.setLiked(liked = liked)
//                        rwChat.insert(chat)
//                    }
//                }
//            }
//        }
    }
}