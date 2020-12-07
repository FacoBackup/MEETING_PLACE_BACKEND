package br.meetingplace.server.modules.message.services.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.message.dao.MI
import br.meetingplace.server.modules.message.dto.response.MessageDTO
import br.meetingplace.server.modules.message.key.AESMessageKey
import java.lang.Exception

object ChatReadService {
    fun readConversation(userID: String, receiverID: String, isGroup: Boolean, date: String,messageDAO: MI, decryption: AESInterface): List<MessageDTO>{
        return try {
            val encryptedConversation = messageDAO.readAllConversation(userID = userID, receiverID = receiverID, isGroup = isGroup, date)
            val decryptedConversation = mutableListOf<MessageDTO>()

            for (i in encryptedConversation.indices){
                val decryptedMessage = decryption.decrypt(myKey = AESMessageKey.key, encryptedConversation[i].content)
                val decryptedImageURL =
                    encryptedConversation[i].imageURL?.let { decryption.decrypt(myKey = AESMessageKey.key, it) }
                if(!decryptedMessage.isNullOrBlank())
                    decryptedConversation.add(MessageDTO(
                        content = decryptedMessage,
                        imageURL = decryptedImageURL,
                        id = encryptedConversation[i].id,
                        creatorID = encryptedConversation[i].creatorID,
                        type = encryptedConversation[i].type,
                        groupID = encryptedConversation[i].groupID,
                        receiverID = encryptedConversation[i].receiverID,
                        creationDate = encryptedConversation[i].creationDate
                    ))
            }
            decryptedConversation
        }catch (e: Exception){
            listOf()
        }
    }
}