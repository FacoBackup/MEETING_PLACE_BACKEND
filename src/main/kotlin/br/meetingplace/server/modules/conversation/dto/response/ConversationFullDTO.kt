package br.meetingplace.server.modules.conversation.dto.response

data class ConversationFullDTO(val id: String,
                               val name: String,
                               val about: String?,
                               val imageURL: String?,
                               val isGroup: Boolean,
                               val creationDate: String,
                               val members: List<ConversationMemberDTO>
                               )
