package br.meetingplace.server.modules.conversation.services.message.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dto.response.messages.MessageDTO

object MessageReadService {
    fun readNewGroupMessages(requester: String, conversationID: String, messageDAO: MI, decryption: AESInterface, conversationMemberDAO: CMI): List<MessageDTO>{
        TODO("NOT YET IMPLEMENTED")
    //        return try {
//
//        }catch (e: Exception){
//            listOf()
//        }
    }
    fun readGroupAllMessages(requester: String, conversationID: String, messageDAO: MI, decryption: AESInterface, conversationMemberDAO: CMI): List<MessageDTO>{
        return try {
            val encryptedConversation: List<MessageDTO> =
                if(conversationMemberDAO.check(conversationID = conversationID, userID = requester))
                messageDAO.readAllConversation(userID = requester, conversationID = conversationID)
            else
                listOf()
            val decryptedConversation = mutableListOf<MessageDTO>()

            for (i in encryptedConversation.indices){
                val decryptedMessage = decryption.decrypt(myKey = AESMessageKey.key, encryptedConversation[i].content)
                val decryptedImageURL = encryptedConversation[i].imageURL?.let { decryption.decrypt(myKey = AESMessageKey.key, it) }
                if(!decryptedMessage.isNullOrBlank())
                    decryptedConversation.add(
                        MessageDTO(
                        content = decryptedMessage,
                        imageURL = decryptedImageURL,
                        id = encryptedConversation[i].id,
                        creatorID = encryptedConversation[i].creatorID,
                        type = encryptedConversation[i].type,
                        conversationID = encryptedConversation[i].conversationID,
                        valid = encryptedConversation[i].valid,
                        creationDate = encryptedConversation[i].creationDate
                    )
                    )
            }
            decryptedConversation
        }catch (e: Exception){
            listOf()
        }
    }
    fun readNewUserMessages(requester: String, userID: String, messageDAO: MI, decryption: AESInterface, conversationOwnerDAO: COI): List<MessageDTO>{
        TODO("NOT YET IMPLEMENTED")
    //        return try {
//            val conversation = conversationOwnerDAO.read(userID = requester, secondUserID = userID)
//
//        }catch (e: Exception){
//            listOf()
//        }
    }
    fun readUserAllMessages(requester: String, userID: String, messageDAO: MI, decryption: AESInterface, conversationOwnerDAO: COI): List<MessageDTO>{
        return try {
            val conversation = conversationOwnerDAO.read(userID = requester, secondUserID = userID)
            val encryptedConversation: List<MessageDTO> =
                if(conversation != null)
                    messageDAO.readAllConversation(userID = requester, conversationID = conversation.conversationID)
                else
                    listOf()
            val decryptedConversation = mutableListOf<MessageDTO>()

            for (i in encryptedConversation.indices){
                val decryptedMessage = decryption.decrypt(myKey = AESMessageKey.key, encryptedConversation[i].content)
                val decryptedImageURL = encryptedConversation[i].imageURL?.let { decryption.decrypt(myKey = AESMessageKey.key, it) }
                if(!decryptedMessage.isNullOrBlank())
                    decryptedConversation.add(
                        MessageDTO(
                            content = decryptedMessage,
                            imageURL = decryptedImageURL,
                            id = encryptedConversation[i].id,
                            creatorID = encryptedConversation[i].creatorID,
                            type = encryptedConversation[i].type,
                            conversationID = encryptedConversation[i].conversationID,
                            valid = encryptedConversation[i].valid,
                            creationDate = encryptedConversation[i].creationDate
                        )
                    )
            }
            decryptedConversation
        }catch (e: Exception){
            listOf()
        }
    }
}