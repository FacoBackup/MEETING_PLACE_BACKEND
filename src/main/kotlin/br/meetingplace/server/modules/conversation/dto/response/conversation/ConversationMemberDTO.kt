package br.meetingplace.server.modules.conversation.dto.response.conversation

data class ConversationMemberDTO(val conversationID: Long,
                                 val admin: Boolean,
                                 val userID: Long)
