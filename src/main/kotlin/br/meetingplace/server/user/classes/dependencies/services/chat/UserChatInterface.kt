package br.meetingplace.server.user.classes.dependencies.services.chat

import br.meetingplace.server.services.chat.classes.SimplifiedChat

interface UserChatInterface {
    fun updateMyChats(chat: SimplifiedChat)
    fun getMyChats(): List<SimplifiedChat>
}