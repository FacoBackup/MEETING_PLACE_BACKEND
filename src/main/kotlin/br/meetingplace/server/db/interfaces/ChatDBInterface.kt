package br.meetingplace.server.db.interfaces

import br.meetingplace.server.modules.chat.dto.Chat

interface ChatDBInterface {
    fun select(id: String): Chat?
    fun insert(data: Chat)
    fun delete(data: Chat)
}