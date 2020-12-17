package br.meetingplace.server.modules.conversation.services.message.factory

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.CI
import br.meetingplace.server.modules.conversation.dao.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationCreation
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*


object MessageFactoryService {
    private const val key = AESMessageKey.key
    fun createMessage(requester: String, data: RequestMessageCreation, conversationMemberDAO: CMI, userDAO: UI, conversationDAO: CI, messageDAO: MI, encryption: AESInterface): HttpStatusCode {
        return try {
            if(userDAO.check(requester))
                when(data.isGroup){
                    true->{
                        val group = data.conversationID?.let { conversationDAO.read(it) }
                        if(group != null && !data.conversationID.isNullOrBlank() && conversationMemberDAO.check(conversationID = data.conversationID, userID = requester)){
                            val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                            val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                            if(encryptedMessage.isNullOrBlank())
                                return HttpStatusCode.InternalServerError
                            else
                                messageDAO.create(message = encryptedMessage, imageURL = encryptedImageURL,from = requester,conversationID = data.conversationID)
                        }
                        else HttpStatusCode.FailedDependency
                    }
                    false->{
                        when(!data.conversationID.isNullOrBlank() && conversationDAO.check(conversationID = data.conversationID)){
                            true->{ //already exists a conversation
                                println("EXISTS ------------------------------------------------")
                                val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                                val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                                if(encryptedMessage.isNullOrBlank())
                                    return HttpStatusCode.InternalServerError
                                else
                                    messageDAO.create(message = encryptedMessage, imageURL = encryptedImageURL,from = requester,conversationID = data.conversationID)
                            }
                            false->{
                                if(data.receiverID != null && userDAO.check(data.receiverID)){
                                    val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                                    val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                                    val id = requester + data.receiverID
                                    conversationDAO.create(data = RequestConversationCreation(name=id, imageURL = null, about = null, isGroup = false), id = id)

                                    if(conversationDAO.check(id)) {
                                        conversationMemberDAO.create(userID = data.receiverID, conversationID = id, true)
                                        conversationMemberDAO.create(userID = requester, conversationID = id, admin = true)
                                        if (encryptedMessage.isNullOrBlank())
                                            return HttpStatusCode.InternalServerError
                                        else
                                            messageDAO.create(
                                                message = encryptedMessage,
                                                imageURL = encryptedImageURL,
                                                from = requester,
                                                conversationID = id
                                            )
                                    }
                                    else HttpStatusCode.FailedDependency
                                }
                                else HttpStatusCode.FailedDependency
                            }
                        }
                    }
                }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

}