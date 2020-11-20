package br.meetingplace.server.modules.chat.dto

import br.meetingplace.server.modules.chat.dto.dependencies.Controller
import br.meetingplace.server.modules.chat.dto.dependencies.owner.ChatOwnerData

class Chat(private var id: String, private var owner: ChatOwnerData) : Controller() {
    fun getOwner() = owner
    fun getID() = id
}