package br.meetingplace.server.modules.topic.services.update

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dao.topic.TopicDAO
import br.meetingplace.server.modules.topic.key.AESTopicKey
import io.ktor.http.*

object TopicUpdateService {
    private const val key = AESTopicKey.key
    suspend fun update(requester: String, topicID: String, body: String?, header: String? , topicDAO: TI, encryption: AESInterface): HttpStatusCode{
        return try{
            val topic =  TopicDAO.read(topicID)
            if(topic != null && topic.creatorID == requester){
                val encryptedHeader = header?.let { encryption.encrypt(key,it) }
                val encryptedBody = body?.let { encryption.encrypt(key,it) }
                topicDAO.update(topicID = topicID, header = encryptedHeader, body = encryptedBody, approved = null)
            }
            else
                HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }

    }
}