package br.meetingplace.server.modules.global.functions.chat

import br.meetingplace.server.modules.chat.dto.dependencies.data.Content

fun getContent(data: List<Content>, ID: String): Content? {
    for (i in data.indices) {
        if (data[i].ID == ID)
            return data[i]
    }
    return null
}