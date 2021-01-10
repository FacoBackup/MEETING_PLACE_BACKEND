package br.meetingplace.server.modules.conversation.services.message.factory

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationCreation
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dto.requests.messages.RequestMessageCreation
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationMemberDTO
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*
import java.util.*


object MessageFactoryService {
    private const val key = AESMessageKey.key

    suspend fun createGroupMessage(requester: String, data: RequestMessageCreation, messageStatusDAO: MSI, conversationMemberDAO: CMI, userDAO: UI, conversationDAO: CI, messageDAO: MI, encryption: AESInterface): HttpStatusCode {
        return try {
            lateinit var messageID: String
            lateinit var conversationMembers: List<ConversationMemberDTO>
            if(userDAO.check(requester)) {
                val group =  conversationDAO.read(data.conversationID)
                if(group != null &&  conversationMemberDAO.check(conversationID = data.conversationID, userID = requester)){
                    val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                    val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                    if(encryptedMessage.isNullOrBlank())
                        return HttpStatusCode.InternalServerError
                    else{
                        messageID = UUID.randomUUID().toString()
                        conversationMembers = conversationMemberDAO.readAllByConversation(conversationID = data.conversationID)
                        val response = messageDAO.create(
                            message = encryptedMessage,
                            imageURL = encryptedImageURL,
                            creator = requester,
                            conversationID = data.conversationID,
                            messageID = messageID)

                        conversationDAO.update(conversationID =  data.conversationID, latestMessage = true,null,null,null)
                        for(i in conversationMembers.indices){
                            messageStatusDAO.create(conversationID = data.conversationID, userID = conversationMembers[i].userID, messageID = messageID)
                        }
                        response
                    }
                }
                else HttpStatusCode.FailedDependency
            }
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    suspend fun createUserMessage(requester: String, data: RequestMessageCreation, messageStatusDAO: MSI, conversationOwnerDAO: COI, userDAO: UI, conversationDAO: CI, messageDAO: MI, encryption: AESInterface):HttpStatusCode{

        return try{
            lateinit var messageID: String
            if(data.receiverID != null){
                val conversation = conversationOwnerDAO.read(userID = requester, secondUserID = data.receiverID)
                when(conversation != null){
                    true->{ //already exists a conversation
                        val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                        val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                        if(encryptedMessage.isNullOrBlank())
                            return HttpStatusCode.InternalServerError
                        else{
                            messageID = UUID.randomUUID().toString()

                            val response = messageDAO.create(
                                message = encryptedMessage,
                                imageURL = encryptedImageURL,
                                creator = requester,
                                conversationID = conversation.conversationID,
                                messageID = messageID)
                            conversationDAO.update(conversationID =  conversation.conversationID, latestMessage =true,null,null,null)
                            messageStatusDAO.create(conversationID = conversation.conversationID, userID = requester, messageID = messageID)
                            messageStatusDAO.create(conversationID = conversation.conversationID, userID = data.receiverID, messageID = messageID)
                            response
                        }

                    }
                    false->{
                        if(userDAO.check(data.receiverID)){
                            val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                            val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                            val id = UUID.randomUUID().toString()
                            conversationDAO.create(
                                data = RequestConversationCreation(
                                    name=requester+data.receiverID,
                                    imageURL = null,
                                    about = null,
                                    isGroup = false),
                                    id = id)

                            if(conversationDAO.check(id)) {
                                conversationOwnerDAO.create(userID = requester, secondUserID = data.receiverID,id)

                                if (encryptedMessage.isNullOrBlank())
                                    return HttpStatusCode.InternalServerError
                                else{
                                    messageID = UUID.randomUUID().toString()

                                    val response = messageDAO.create(
                                        message = encryptedMessage,
                                        imageURL = encryptedImageURL,
                                        creator = requester,
                                        conversationID = id,
                                        messageID = messageID
                                    )
                                    conversationDAO.update(conversationID = id, latestMessage = true,null,null,null)
                                    messageStatusDAO.create(conversationID = id, userID = requester, messageID = messageID)
                                    messageStatusDAO.create(conversationID = id, userID = data.receiverID, messageID = messageID)
                                    response
                                }

                            }
                            else HttpStatusCode.BadGateway
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