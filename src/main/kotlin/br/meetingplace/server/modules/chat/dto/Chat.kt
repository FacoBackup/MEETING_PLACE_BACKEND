package br.meetingplace.server.modules.chat.dto

import br.meetingplace.server.modules.chat.dto.dependencies.data.Content
import br.meetingplace.server.modules.owner.dto.OwnerData

class Chat(private val id: String, private val owner: OwnerData) {
    private var messages = listOf<Content>()
    private var liked = listOf<String>()

    fun getMessages () = messages
    fun getLiked () = liked
    fun getOwner() = owner
    fun getID() = id

    fun setLiked(liked: List<String>){
        this.liked = liked
    }
    fun setMessages (messages: List<Content>){
        this.messages = messages
    }
}