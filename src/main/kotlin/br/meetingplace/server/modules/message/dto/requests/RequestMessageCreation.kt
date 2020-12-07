package br.meetingplace.server.modules.message.dto.requests

data class RequestMessageCreation(val message: String,
                                  val imageURL: String?,
                                  val receiverID: String,
                                  val isGroup: Boolean)
