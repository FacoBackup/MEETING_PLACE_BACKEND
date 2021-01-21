package br.meetingplace.server.modules.conversation.dto.response.conversation

data class ConversationDTO(val id: Long,
                           val name: String,
                           val about: String?,
                           val imageURL: String?,
                           val isGroup: Boolean,
                           val creationDate: Long,
                           val latestMessage: Long?,

)