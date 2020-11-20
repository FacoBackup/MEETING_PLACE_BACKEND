package br.meetingplace.server.modules.chat.dao.search

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.requests.chat.ChatFinderOperator

class ChatSearch private constructor() {
    companion object {
        private val Class = ChatSearch()
        fun getClass() = Class
    }


    fun seeChat(data: ChatFinderOperator, rwUser: UserDBInterface, rwChat: ChatDBInterface, rwCommunity: CommunityDBInterface, rwGroup: GroupDBInterface): Chat? {
        val user = rwUser.select(data.login.email)

        if (user != null) {
            when (data.identifier.communityGroup || data.identifier.userGroup) {
                true -> { //IS GROUP
                    when (data.identifier.communityGroup) {
                        true -> {//COMMUNITY GROUP
                            val group = rwGroup.select(data.identifier.receiverID)
                            if(group != null){
                                val community = rwCommunity.select(group.getOwner().groupOwnerID)
                                if(community!= null && community.checkGroupApproval(data.identifier.receiverID))
                                    return rwChat.select(group.getChatID())
                            }
                        }
                        false -> { //USER GROUP
                            return rwChat.select(data.identifier.chatID)
                        }
                    }
                }
                false -> { //IS USER <-> USER
                    return rwChat.select(data.identifier.chatID)
                }
            }
        }
        return null
    }
}