package br.meetingplace.server.routers.chat.requests

import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageType
import br.meetingplace.server.routers.generic.requests.Login

data class MessageData(var message: String, val messageType: MessageType?, val receiver: ChatIdentifier, val login: Login)
