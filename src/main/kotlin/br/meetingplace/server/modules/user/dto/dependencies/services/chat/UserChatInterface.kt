package br.meetingplace.server.modules.user.dto.dependencies.services.chat

import br.meetingplace.server.modules.chat.dto.SimplifiedChat

interface UserChatInterface {
    fun updateMyChats(chat: SimplifiedChat)
    fun getMyChats(): List<SimplifiedChat>
}