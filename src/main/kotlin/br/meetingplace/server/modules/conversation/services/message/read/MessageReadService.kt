package br.meetingplace.server.modules.conversation.services.message.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dto.response.messages.MessageDTO
import br.meetingplace.server.modules.conversation.dto.response.messages.MessageStatusDTO
import br.meetingplace.server.modules.user.dao.user.UI

object MessageReadService {

//
//    fun readNewGroupMessages(requester: String, messageStatusDAO: MSI, conversationID: String, messageDAO: MI, decryption: AESInterface, conversationMemberDAO: CMI): List<MessageDTO>{
//        TODO("NOT YET IMPLEMENTED")
////        return try {
////
////        }catch (e: Exception){
////            listOf()
////        }
//    }
    suspend fun readGroupAllMessages(
        requester: String,
        conversationID: String,
        messageStatusDAO: MSI,
        messageDAO: MI,
        decryption: AESInterface,
        conversationMemberDAO: CMI): List<MessageDTO>{

        return try {
            val encryptedMessages: List<MessageDTO> =
                if(conversationMemberDAO.check(conversationID = conversationID, userID = requester))
                messageDAO.readAllConversation(userID = requester, conversationID = conversationID)
            else
                listOf()
            val decryptedConversation = mutableListOf<MessageDTO>()

            for (i in encryptedMessages.indices){
                val decryptedMessage = decryption.decrypt(myKey = AESMessageKey.key, encryptedMessages[i].content)
                val decryptedImageURL = encryptedMessages[i].imageURL?.let { decryption.decrypt(myKey = AESMessageKey.key, it) }
                if(!decryptedMessage.isNullOrBlank()){
                    decryptedConversation.add(
                        MessageDTO(
                            content = decryptedMessage,
                            imageURL = decryptedImageURL,
                            id = encryptedMessages[i].id,
                            creatorID = encryptedMessages[i].creatorID,
                            type = encryptedMessages[i].type,
                            conversationID = encryptedMessages[i].conversationID,
                            valid = encryptedMessages[i].valid,
                            creationDate = encryptedMessages[i].creationDate,
                            seenByEveryone = encryptedMessages[i].seenByEveryone,
                            receiverAsUserID = null
                        )
                    )

                    messageStatusDAO.update(
                        conversationID = encryptedMessages[i].conversationID,
                        userID = requester,
                        messageID = encryptedMessages[i].id)

                    if(messageStatusDAO.seenByEveryoneByMessage(messageID = encryptedMessages[i].id, conversationID = encryptedMessages[i].conversationID)){
                        messageDAO.update(encryptedMessages[i].id)
                    }
                }
            }
            decryptedConversation
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readNewUserMessages(
        requester: String,
        userDAO: UI,
        userID: String,
        messageStatusDAO: MSI,
        messageDAO: MI, decryption: AESInterface,
        conversationOwnerDAO: COI): List<MessageDTO>{

        return try {
            lateinit var unseenMessages: List<MessageStatusDTO>
            val conversation = conversationOwnerDAO.read(userID = requester, secondUserID = userID)

            if(conversation != null && userDAO.check(requester) && userDAO.check(userID)) {
                unseenMessages = messageStatusDAO.readAllUnseenMessages(conversationID = conversation.conversationID, userID = requester)
                val decryptedMessages = mutableListOf<MessageDTO>()

                for (i in unseenMessages.indices){
                    val encryptedMessage = messageDAO.read(unseenMessages[i].messageID)
                    if (encryptedMessage != null){
                        val messageContent = decryption.decrypt(AESMessageKey.key, data = encryptedMessage.content)
                        val messageImage = encryptedMessage.imageURL?.let { decryption.decrypt(AESMessageKey.key, data = it) }

                        if(messageContent != null){
                            decryptedMessages.add(
                                MessageDTO(
                                    content = messageContent,
                                    imageURL = messageImage,
                                    id = encryptedMessage.id,
                                    creatorID = encryptedMessage.creatorID,
                                    type = encryptedMessage.type,
                                    conversationID = encryptedMessage.conversationID,
                                    valid = encryptedMessage.valid,
                                    creationDate = encryptedMessage.creationDate,
                                    seenByEveryone = encryptedMessage.seenByEveryone,
                                    receiverAsUserID = if(conversation.secondaryUserID != requester) conversation.secondaryUserID else conversation.primaryUserID
                                )
                            )
                            messageStatusDAO.update(conversationID = conversation.conversationID,
                                userID = requester,
                                messageID = encryptedMessage.id)
                            if(messageStatusDAO.seenByEveryoneByMessage(messageID = encryptedMessage.id, conversationID = encryptedMessage.conversationID)){
                                messageDAO.update(encryptedMessage.id)
                            }
                        }
                    }
                }
                decryptedMessages //RETURN
            }
            else
                listOf()
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readUserAllMessages(
        requester: String,
        userID: String,
        messageStatusDAO: MSI,
        messageDAO: MI,
        decryption: AESInterface,
        conversationOwnerDAO: COI): List<MessageDTO>{
        return try {
            val conversation = conversationOwnerDAO.read(userID = requester, secondUserID = userID)
            val encryptedMessages: List<MessageDTO> =
                if(conversation != null)
                    messageDAO.readAllConversation(userID = requester, conversationID = conversation.conversationID)
                else
                    listOf()
            val decryptedConversation = mutableListOf<MessageDTO>()

            for (i in encryptedMessages.indices){
                val decryptedMessage = decryption.decrypt(myKey = AESMessageKey.key, encryptedMessages[i].content)
                val decryptedImageURL = encryptedMessages[i].imageURL?.let { decryption.decrypt(myKey = AESMessageKey.key, it) }
                if(!decryptedMessage.isNullOrBlank())
                    decryptedConversation.add(
                        MessageDTO(
                            content = decryptedMessage,
                            imageURL = decryptedImageURL,
                            id = encryptedMessages[i].id,
                            creatorID = encryptedMessages[i].creatorID,
                            type = encryptedMessages[i].type,
                            conversationID = encryptedMessages[i].conversationID,
                            valid = encryptedMessages[i].valid,
                            creationDate = encryptedMessages[i].creationDate,
                            seenByEveryone = encryptedMessages[i].seenByEveryone,
                            receiverAsUserID = if(conversation != null && conversation.secondaryUserID != requester) conversation.secondaryUserID else if(conversation != null && conversation.primaryUserID != requester) conversation.primaryUserID else null
                        )
                    )
                messageStatusDAO.update(
                    conversationID = encryptedMessages[i].conversationID,
                    userID = requester,
                    messageID = encryptedMessages[i].id)

                if(messageStatusDAO.seenByEveryoneByMessage(messageID = encryptedMessages[i].id, conversationID = encryptedMessages[i].conversationID)){
                    messageDAO.update(encryptedMessages[i].id)
                }
            }
            decryptedConversation
        }catch (e: Exception){
            listOf()
        }
    }
}