package br.meetingplace.server.modules.messageTODO.dto.requests

data class RequestConversation(val receiverID: String, val userID: String, val isGroup: Boolean)
