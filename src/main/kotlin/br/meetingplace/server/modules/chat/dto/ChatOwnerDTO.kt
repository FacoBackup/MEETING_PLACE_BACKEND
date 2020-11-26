package br.meetingplace.server.modules.chat.dto

data class ChatOwnerDTO(val chatID: String, val userID: String,
                        val receiverID: String)