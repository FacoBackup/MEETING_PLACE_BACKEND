package br.meetingplace.server.modules.chat.dto

import br.meetingplace.server.modules.chat.dto.dependencies.data.Content
import br.meetingplace.server.modules.global.dto.owner.OwnerData

class Chat(private val id: String, private val owner: OwnerData) {
    private var messages = mutableListOf<Content>()
    private var liked = mutableListOf<String>()

    fun getMessages() = messages
    fun getLiked() = liked
    fun getOwner() = owner
    fun getID() = id

    fun setLiked(liked: MutableList<String>) {
        this.liked = liked
    }

    fun setMessages(messages: MutableList<Content>) {
        this.messages = messages
    }
}