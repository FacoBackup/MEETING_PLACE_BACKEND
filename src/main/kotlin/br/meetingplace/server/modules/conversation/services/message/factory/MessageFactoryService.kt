package br.meetingplace.server.modules.conversation.services.message.factory

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.CI
import br.meetingplace.server.modules.conversation.dao.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.RequestConversationCreation
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.owners.COI
import br.meetingplace.server.modules.conversation.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*
import java.util.*


object MessageFactoryService {
    private const val key = AESMessageKey.key
    fun createGroupMessage(requester: String, data: RequestMessageCreation, conversationMemberDAO: CMI, userDAO: UI, conversationDAO: CI, messageDAO: MI, encryption: AESInterface): HttpStatusCode {
        return try {
            if(userDAO.check(requester)) {
                val group =  conversationDAO.read(data.conversationID)
                if(group != null &&  conversationMemberDAO.check(conversationID = data.conversationID, userID = requester)){
                    val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                    val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                    if(encryptedMessage.isNullOrBlank())
                        return HttpStatusCode.InternalServerError
                    else
                        messageDAO.create(message = encryptedMessage, imageURL = encryptedImageURL,from = requester,conversationID = data.conversationID)
                }
                else HttpStatusCode.FailedDependency
            }
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun createUserMessage(requester: String, data: RequestMessageCreation, conversationOwnerDAO: COI, userDAO: UI, conversationDAO: CI, messageDAO: MI, encryption: AESInterface):HttpStatusCode{
        return try{
            if(userDAO.check(requester) && data.receiverID != null){
                val conversation = conversationOwnerDAO.read(userID = requester, secondUserID = data.receiverID)
                when(conversation != null){
                    true->{ //already exists a conversation
                        val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                        val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                        if(encryptedMessage.isNullOrBlank())
                            return HttpStatusCode.InternalServerError
                        else
                            messageDAO.create(message = encryptedMessage, imageURL = encryptedImageURL,from = requester,conversationID = conversation.conversationID)

                    }
                    false->{

                        if(userDAO.check(data.receiverID)){
                            val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                            val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                            val id = UUID.randomUUID().toString()

                            conversationDAO.create(data = RequestConversationCreation(name=requester+data.receiverID, imageURL = null, about = null, isGroup = false), id = id)

                            if(conversationDAO.check(id)) {

                                conversationOwnerDAO.create(userID = requester, secondUserID = data.receiverID,id)

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
            else HttpStatusCode.FailedDependency

        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}