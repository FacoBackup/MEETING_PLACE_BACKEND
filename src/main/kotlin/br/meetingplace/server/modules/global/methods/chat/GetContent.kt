package br.meetingplace.server.modules.global.methods.chat

import br.meetingplace.server.modules.chat.classes.message.Message

fun getContent(data: List<Message>, ID: String): Message? {
    for (i in data.indices) {
        if (data[i].ID == ID)
            return data[i]
    }
    return null
}