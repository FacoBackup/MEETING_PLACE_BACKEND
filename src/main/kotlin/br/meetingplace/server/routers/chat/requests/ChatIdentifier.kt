package br.meetingplace.server.routers.chat.requests

data class ChatIdentifier(val chatID: String, val receiverID: String, val communityGroup: Boolean, val userGroup: Boolean)