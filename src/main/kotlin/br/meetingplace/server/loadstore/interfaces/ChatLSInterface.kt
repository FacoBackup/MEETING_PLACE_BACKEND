package br.meetingplace.server.loadstore.interfaces

import br.meetingplace.server.services.chat.classes.Chat

interface ChatLSInterface {
    fun load(id: String): Chat?
    fun store(data: Chat)
    fun delete(data: Chat)
}