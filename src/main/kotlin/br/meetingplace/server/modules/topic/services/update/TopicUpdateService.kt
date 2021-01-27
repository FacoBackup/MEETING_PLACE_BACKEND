package br.meetingplace.server.modules.topic.services.update

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.topic.dao.tag.TTI
import br.meetingplace.server.modules.topic.dao.tag.TagInterface
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dao.topic.TopicDAO
import br.meetingplace.server.modules.topic.key.AESTopicKey
import io.ktor.http.*

object TopicUpdateService {
    private const val key = AESTopicKey.key
    suspend fun update(requester: Long, topicID: Long, body: String?, header: String?, tagDAO: TagInterface, topicTagDAO: TTI,
                       hashTags:List<String>, topicDAO: TI, encryption: AESInterface): HttpStatusCode{
        return try{
            val topic =  TopicDAO.read(topicID)
            if(topic != null && topic.creatorID == requester){
                val encryptedHeader = header?.let { encryption.encrypt(key,it) }
                val encryptedBody = body?.let { encryption.encrypt(key,it) }
                val result = topicDAO.update(topicID = topicID, header = encryptedHeader, body = encryptedBody, approved = null)

                if(result == HttpStatusCode.OK && body != null && topic.mainTopicID == null){
                    topicTagDAO.delete(topicID = topicID)
                    for(i in hashTags.indices){
                        val tag = tagDAO.read(hashTags[i].toLowerCase())
                        if(tag != null){
                            topicTagDAO.create(topicID = topicID, tagID= tag.tagID)
                        }
                        else{
                            val tagID = tagDAO.create(hashTags[i].toLowerCase())
                            if(tagID != null)
                                topicTagDAO.create(topicID = topicID, tagID= tagID)
                        }
                    }
                }

                result
            }
            else
                HttpStatusCode.FailedDependency
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }

    }
}