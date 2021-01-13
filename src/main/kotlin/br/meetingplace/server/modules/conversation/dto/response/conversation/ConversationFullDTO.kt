package br.meetingplace.server.modules.conversation.dto.response.conversation

data class ConversationFullDTO(val id: String,
                               val name: String,
                               val about: String?,
                               val imageURL: String?,
                               val isGroup: Boolean,
                               val creationDate: Long,
                               val members: List<ConversationMemberDTO>,
                               val unreadMessages: Long,
                               val userName: String?,
                               val latestMessage: Long?
                               )
