package br.meetingplace.server.requests.chat

data class ChatIdentifier(val chatID: String, val receiverID: String, val communityGroup: Boolean, val userGroup: Boolean)