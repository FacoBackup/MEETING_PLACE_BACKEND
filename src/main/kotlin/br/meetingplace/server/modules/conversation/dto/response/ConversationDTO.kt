package br.meetingplace.server.modules.conversation.dto.response

data class ConversationDTO(val id: String,
                           val name: String,
                           val about: String?,
                           val imageURL: String?,
                           val isGroup: Boolean,
                           val creationDate: String)