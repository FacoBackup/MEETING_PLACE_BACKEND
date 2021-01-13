package br.meetingplace.server.modules.conversation.dto.requests.conversation

data class RequestConversationCreation(val name: String,
                                       val imageURL: String?,
                                       val about: String?,
                                       val isGroup: Boolean)