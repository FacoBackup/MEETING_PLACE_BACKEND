package br.meetingplace.server.modules.user.dto.dependencies.services.chat

import br.meetingplace.server.modules.chat.dto.ChatIdentifier

interface UserChatInterface {
    fun updateMyChats(chat: ChatIdentifier)
    fun getMyChats(): List<ChatIdentifier>
}