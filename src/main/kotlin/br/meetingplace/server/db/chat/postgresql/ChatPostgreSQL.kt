package br.meetingplace.server.db.chat.postgresql

import br.meetingplace.server.db.chat.ChatDBInterface
import br.meetingplace.server.modules.chat.dto.Chat
import br.meetingplace.server.modules.global.dto.http.status.Status

class ChatPostgreSQL private constructor() : ChatDBInterface {
    companion object {
        private val Class = ChatPostgreSQL()
        fun getClass() = Class
    }

    override fun check(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(data: Chat): Status {
        TODO("Not yet implemented")
    }

    override fun insert(data: Chat): Status {
        TODO("Not yet implemented")
    }

    override fun select(id: String): Chat? {
        TODO("Not yet implemented")
    }
}