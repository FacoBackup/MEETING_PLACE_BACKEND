package br.meetingplace.server.db.chat

import br.meetingplace.server.modules.chat.classes.Chat
import br.meetingplace.server.modules.global.dto.http.status.Status

interface ChatDBInterface {
    fun select(id: String): Chat?
    fun insert(data: Chat): Status
    fun delete(data: Chat): Status
    fun check(id: String): Boolean
}