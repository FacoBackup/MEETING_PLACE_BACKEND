package br.meetingplace.server.modules.chat.dto

import br.meetingplace.server.modules.chat.dto.dependencies.data.Content
import br.meetingplace.server.modules.owner.dto.OwnerData

class Chat(private val id: String, private val owner: OwnerData) {
    private val messages = mutableListOf<Content>()
    private val liked = mutableListOf<String>()

    fun getOwner() = owner
    fun getID() = id
}