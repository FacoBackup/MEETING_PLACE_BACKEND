package br.meetingplace.server.modules.user.dto.dependencies.services.chat

import br.meetingplace.server.modules.chat.dto.ChatIdentifier

class UserChat private constructor() : UserChatInterface {

    private var myChats = mutableListOf<ChatIdentifier>()

    companion object {
        private val Class = UserChat()
        fun getClass() = Class
    }

    override fun updateMyChats(chat: ChatIdentifier) {
        if (chat !in myChats)
            myChats.add(chat)
    }

    override fun getMyChats() = myChats
}