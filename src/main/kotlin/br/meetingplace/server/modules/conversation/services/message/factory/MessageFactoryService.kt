package br.meetingplace.server.modules.conversation.services.message.factory

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationCreation
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dao.notification.MNI
import br.meetingplace.server.modules.conversation.dto.requests.messages.RequestMessageCreation
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationMemberDTO
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*
import java.util.*


object MessageFactoryService {
    private const val key = AESMessageKey.key

    suspend fun createGroupMessage(requester: Long, data: RequestMessageCreation,messageNotificationDAO: MNI, messageStatusDAO: MSI, conversationMemberDAO: CMI, userDAO: UI, conversationDAO: CI, messageDAO: MI, encryption: AESInterface): HttpStatusCode {
        return try {
            lateinit var conversationMembers: List<ConversationMemberDTO>
            if(userDAO.check(requester)) {
                val group =  conversationDAO.read(data.conversationID)
                if(group != null &&  conversationMemberDAO.check(conversationID = data.conversationID, userID = requester)){
                    val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                    val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                    if(encryptedMessage.isNullOrBlank())
                        return HttpStatusCode.InternalServerError
                    else{

                        conversationMembers = conversationMemberDAO.readAllByConversation(conversationID = data.conversationID)
                        val response = messageDAO.create(
                            message = encryptedMessage,
                            imageURL = encryptedImageURL,
                            creator = requester,
                            conversationID = data.conversationID)

                       if(response != null){
                           conversationDAO.update(conversationID =  data.conversationID, latestMessage = true,null,null,null)
                           for(i in conversationMembers.indices){
                               messageNotificationDAO.create(conversationMembers[i].userID, conversationID = data.conversationID, messageID = response,isGroup = true, creationDate = System.currentTimeMillis())
                               messageStatusDAO.create(conversationID = data.conversationID, userID = conversationMembers[i].userID, messageID = response)
                           }
                           HttpStatusCode.Created
                        }
                        else
                            HttpStatusCode.InternalServerError

                    }
                }
                else HttpStatusCode.FailedDependency
            }
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    suspend fun createUserMessage(requester: Long, data: RequestMessageCreation, messageNotificationDAO: MNI, messageStatusDAO: MSI, conversationOwnerDAO: COI, userDAO: UI, conversationDAO: CI, messageDAO: MI, encryption: AESInterface):HttpStatusCode{

        return try{
            if(data.receiverID != null){
                val conversation = conversationOwnerDAO.read(userID = requester, secondUserID = data.receiverID)
                when(conversation != null){
                    true->{ //already exists a conversation
                        val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                        val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                        if(encryptedMessage.isNullOrBlank())
                            return HttpStatusCode.ExpectationFailed
                        else{

                            val response = messageDAO.create(
                                message = encryptedMessage,
                                imageURL = encryptedImageURL,
                                creator = requester,
                                conversationID = conversation.conversationID)

                            return if(response != null){
                                messageNotificationDAO.create(conversationID = conversation.conversationID, messageID = response,isGroup = false,creationDate= System.currentTimeMillis(), requester = data.receiverID)
                                conversationDAO.update(conversationID =  conversation.conversationID, latestMessage =true,null,null,null)
                                messageStatusDAO.create(conversationID = conversation.conversationID, userID = requester, messageID = response)
                                messageStatusDAO.create(conversationID = conversation.conversationID, userID = data.receiverID, messageID = response)
                                HttpStatusCode.Created
                            }
                            else
                                HttpStatusCode.InternalServerError

                        }

                    }
                    false->{
                        if(userDAO.check(data.receiverID)){
                            val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                            val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }

                            val id = conversationDAO.create(
                                data = RequestConversationCreation(
                                    name="private",
                                    imageURL = null,
                                    about = null,
                                    isGroup = false))

                            if(id != null && conversationDAO.check(id)) {
                                conversationOwnerDAO.create(userID = requester, secondUserID = data.receiverID,id)

                                if (encryptedMessage.isNullOrBlank())
                                    return HttpStatusCode.InternalServerError
                                else{

                                    val response = messageDAO.create(
                                        message = encryptedMessage,
                                        imageURL = encryptedImageURL,
                                        creator = requester,
                                        conversationID = id
                                    )
                                    if(response != null){
                                        messageNotificationDAO.create( conversationID = id,isGroup = false, creationDate = System.currentTimeMillis(), messageID = response, requester = data.receiverID)
                                        conversationDAO.update(conversationID = id, latestMessage = true,null,null,null)
                                        messageStatusDAO.create(conversationID = id, userID = requester, messageID = response)
                                        messageStatusDAO.create(conversationID = id, userID = data.receiverID, messageID = response)
                                        HttpStatusCode.Created
                                    }
                                    else
                                        HttpStatusCode.InternalServerError

                                }
                            }
                            else HttpStatusCode.InternalServerError
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