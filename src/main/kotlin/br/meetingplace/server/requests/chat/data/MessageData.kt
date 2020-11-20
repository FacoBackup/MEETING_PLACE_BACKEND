package br.meetingplace.server.requests.chat.data

import br.meetingplace.server.modules.chat.dto.dependencies.data.MessageType
import br.meetingplace.server.requests.generic.data.Login

data class MessageData(var message: String, val imageURL: String?, val messageType: MessageType?, val receiver: ChatIdentifier, val login: Login)
