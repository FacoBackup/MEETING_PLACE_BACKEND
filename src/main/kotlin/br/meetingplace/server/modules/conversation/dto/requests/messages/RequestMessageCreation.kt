package br.meetingplace.server.modules.conversation.dto.requests.messages

data class RequestMessageCreation(val message: String,
                                  val imageURL: String?,
                                  val receiverID: Long?,
                                  val conversationID: Long,
                                  val isGroup: Boolean)
