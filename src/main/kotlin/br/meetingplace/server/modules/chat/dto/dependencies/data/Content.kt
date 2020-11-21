package br.meetingplace.server.modules.chat.dto.dependencies.data

data class Content(var content: String, val imageURL: String?, val ID: String, val creator: String, val type: MessageType)