package br.meetingplace.server.modules.message.services.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.message.dao.MI
import br.meetingplace.server.modules.message.dto.response.MessageDTO
import br.meetingplace.server.modules.message.control.encryption.key.AESMessageKey
import java.lang.Exception

object ChatReadService {
    fun readConversation(requester: String, subjectID: String, isGroup: Boolean, messageDAO: MI, decryption: AESInterface): List<MessageDTO>{
        return try {
            val encryptedConversation = messageDAO.readAllConversation(userID = requester, subjectID = subjectID, isGroup = isGroup)
            val decryptedConversation = mutableListOf<MessageDTO>()

            for (i in encryptedConversation.indices){
                val decryptedMessage = decryption.decrypt(myKey = AESMessageKey.key, encryptedConversation[i].content)
                val decryptedImageURL = encryptedConversation[i].imageURL?.let { decryption.decrypt(myKey = AESMessageKey.key, it) }
                if(!decryptedMessage.isNullOrBlank())
                    decryptedConversation.add(MessageDTO(
                        content = decryptedMessage,
                        imageURL = decryptedImageURL,
                        id = encryptedConversation[i].id,
                        creatorID = encryptedConversation[i].creatorID,
                        type = encryptedConversation[i].type,
                        groupID = encryptedConversation[i].groupID,
                        receiverID = encryptedConversation[i].receiverID,
                        valid = encryptedConversation[i].valid
                    ))
            }
            decryptedConversation
        }catch (e: Exception){
            listOf()
        }
    }
}