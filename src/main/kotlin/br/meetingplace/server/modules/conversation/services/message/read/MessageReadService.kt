package br.meetingplace.server.modules.conversation.services.message.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.member.CMI
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dto.response.MessageDTO

object MessageReadService {
    fun readConversationMessages(requester: String, conversationID: String, messageDAO: MI, decryption: AESInterface, conversationMemberDAO: CMI): List<MessageDTO>{
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
                        received = encryptedConversation[i].received,
                        valid = encryptedConversation[i].valid,
                        read = encryptedConversation[i].read,
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