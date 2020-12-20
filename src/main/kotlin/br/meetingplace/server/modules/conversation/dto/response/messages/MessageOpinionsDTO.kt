package br.meetingplace.server.modules.conversation.dto.response.messages

data class MessageOpinionsDTO(var liked: Boolean,
                              val messageID: String,
                              val userID: String)
