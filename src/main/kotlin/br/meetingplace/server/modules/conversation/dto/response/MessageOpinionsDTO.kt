package br.meetingplace.server.modules.conversation.dto.response

data class MessageOpinionsDTO(var liked: Boolean,
                              val messageID: String,
                              val userID: String)
