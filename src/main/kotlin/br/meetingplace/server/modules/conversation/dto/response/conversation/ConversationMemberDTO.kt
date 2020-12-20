package br.meetingplace.server.modules.conversation.dto.response.conversation

data class ConversationMemberDTO(val conversationID: String,
                                 var admin: Boolean,
                                 val userID: String)
