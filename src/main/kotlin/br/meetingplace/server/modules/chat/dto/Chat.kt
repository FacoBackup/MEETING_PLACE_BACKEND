package br.meetingplace.server.modules.chat.dto

import br.meetingplace.server.modules.chat.dto.dependencies.Controller

class Chat(private var id: String, private var owner: ChatOwnerData) : Controller() {
    fun getOwner() = owner
    fun getID() = id
}