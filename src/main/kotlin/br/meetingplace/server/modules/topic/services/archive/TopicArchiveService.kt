package br.meetingplace.server.modules.topic.services.archive

import br.meetingplace.server.modules.topic.dao.archive.TAI
import io.ktor.http.*

object TopicArchiveService {
    suspend fun archiveTopic(topicID: String, requester: String, topicArchiveDAO: TAI): HttpStatusCode{
        return try{
            return if(!topicArchiveDAO.check(topicID, requester)){
                topicArchiveDAO.create(topicID, requester)
            }else{
                topicArchiveDAO.delete(topicID, requester)
            }
        }catch (e: Exception){
            HttpStatusCode.InternalServerError
        }
    }
}