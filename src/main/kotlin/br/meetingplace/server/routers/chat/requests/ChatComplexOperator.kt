package br.meetingplace.server.routers.chat.requests

import br.meetingplace.server.routers.generic.requests.Login

data class ChatComplexOperator(
        var message: String,
        var messageID: String,
        val source: ChatIdentifier,
        val receiver: ChatIdentifier,
        val login: Login
)