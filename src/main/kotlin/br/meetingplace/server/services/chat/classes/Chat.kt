package br.meetingplace.server.services.chat.classes

import br.meetingplace.server.services.chat.classes.dependencies.Controller
import br.meetingplace.server.data.classes.owner.chat.ChatOwnerData

class Chat(private var id: String, private var owner: ChatOwnerData) : Controller() {
    fun getOwner() = owner
    fun getID() = id
}