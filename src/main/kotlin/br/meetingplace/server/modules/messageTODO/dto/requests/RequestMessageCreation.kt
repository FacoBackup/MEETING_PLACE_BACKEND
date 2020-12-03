package br.meetingplace.server.modules.messageTODO.dto.requests

data class RequestMessageCreation(val message: String, val imageURL: String?, val receiverID: String, val userID: String, val isGroup: Boolean)
