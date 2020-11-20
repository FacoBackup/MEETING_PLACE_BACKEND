package br.meetingplace.server.routers.chat.requests

import br.meetingplace.server.routers.generic.requests.Login

data class ChatSimpleOperator(var messageID: String, val receiver: ChatIdentifier, val login: Login)