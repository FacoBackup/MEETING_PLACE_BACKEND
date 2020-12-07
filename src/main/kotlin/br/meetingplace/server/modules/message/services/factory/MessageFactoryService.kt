package br.meetingplace.server.modules.message.services.factory

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.modules.message.dao.MI
import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.message.key.AESMessageKey
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*


object MessageFactoryService {
    private const val key = AESMessageKey.key
    fun createMessage(requester: String,data: RequestMessageCreation, groupMemberDAO: GMI, userDAO: UI, groupDAO: GI, messageDAO: MI, encryption: AESInterface): HttpStatusCode {
        return try {
            if(userDAO.check(requester))
                when(data.isGroup){
                    true->{
                        val group =groupDAO.read(data.receiverID)
                        if(group != null && group.approved && groupMemberDAO.read(groupID = data.receiverID, userID = requester) != null){

                            val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                            val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                            if(encryptedMessage.isNullOrBlank())
                                return HttpStatusCode.InternalServerError
                            else
                                messageDAO.create(message = encryptedMessage, imageURL = encryptedImageURL,from = requester,to = data.receiverID,isGroup = true)
                        }
                        else HttpStatusCode.FailedDependency
                    }
                    false->{
                        if(userDAO.check(data.receiverID)){
                            val encryptedMessage = encryption.encrypt(myKey = key, data.message)
                            val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                            if(encryptedMessage.isNullOrBlank())
                                return HttpStatusCode.InternalServerError
                            else
                                messageDAO.create(message = encryptedMessage, imageURL = encryptedImageURL,from = requester,to = data.receiverID,isGroup = false)
                        }

                        else HttpStatusCode.FailedDependency

                    }
                }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

}