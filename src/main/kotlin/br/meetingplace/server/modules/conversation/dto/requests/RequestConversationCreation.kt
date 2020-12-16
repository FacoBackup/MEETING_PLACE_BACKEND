package br.meetingplace.server.modules.conversation.dto.requests

data class RequestConversationCreation(val name: String,
                                       val imageURL: String?,
                                       val about: String?,
                                       val isGroup: Boolean)