package br.meetingplace.server.db.chat

import br.meetingplace.server.modules.chat.dto.Chat

interface ChatDBInterface {
    fun select(id: String): Chat?
    fun insert(data: Chat)
    fun delete(data: Chat)
    fun check(id: String): Boolean
}