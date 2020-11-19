package br.meetingplace.server.services.chat.controller.search

import br.meetingplace.server.loadstore.interfaces.ChatLSInterface
import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.chat.ChatFinderOperator
import br.meetingplace.server.services.chat.classes.Chat

class ChatSearch private constructor() {
    companion object {
        private val Class = ChatSearch()
        fun getClass() = Class
    }


    fun seeChat(data: ChatFinderOperator, rwUser: UserLSInterface, rwChat: ChatLSInterface, rwCommunity: CommunityLSInterface, rwGroup: GroupLSInterface): Chat? {
        val user = rwUser.load(data.login.email)

        if (user != null) {
            when (data.identifier.communityGroup || data.identifier.userGroup) {
                true -> { //IS GROUP
                    when (data.identifier.communityGroup) {
                        true -> {//COMMUNITY GROUP
                            val group = rwGroup.read(data.identifier.receiverID)
                            if(group != null){
                                val community = rwCommunity.load(group.getOwner().groupOwnerID)
                                if(community!= null && community.checkGroupApproval(data.identifier.receiverID))
                                    return rwChat.load(group.getChatID())
                            }
                        }
                        false -> { //USER GROUP
                            return rwChat.load(data.identifier.chatID)
                        }
                    }
                }
                false -> { //IS USER <-> USER
                    return rwChat.load(data.identifier.chatID)
                }
            }
        }
        return null
    }
}