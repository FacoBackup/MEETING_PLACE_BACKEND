package br.meetingplace.server.modules.conversation.dto.requests.messages

data class RequestMessageCreation(val message: String,
                                  val imageURL: String?,
                                  val receiverID: String?,
                                  val conversationID: String,
                                  val isGroup: Boolean)
