package br.meetingplace.server.modules.topic.services.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.topic.dao.seen.TVI
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.key.AESTopicKey
import br.meetingplace.server.modules.user.dao.social.SI

object TopicReadService {
    private const val key = AESTopicKey.key

    suspend fun readAllTimeline(requester: String, topicDAO: TI, userSocialDAO: SI, decryption: AESInterface, topicVisualizationDAO: TVI): List<TopicDTO>{
        return try{
            val timePeriod = System.currentTimeMillis() - 259200000
            val following = userSocialDAO.readAll(requester, following = true)
            val timeline = mutableListOf<TopicDTO>()
            for(i in following.indices){
                val topics = topicDAO.readByTimePeriod(following[i].followedID, timePeriod, false)
                val decryptedTopics= mutableListOf<TopicDTO>()
                for(j in topics.indices){
                    val header =decryption.decrypt(myKey = key, data = topics[j].header)
                    val body = decryption.decrypt(myKey = key, data = topics[j].body)
                    val imageURL = topics[j].imageURL?.let { decryption.decrypt(myKey = key, data = it) }
                    if(!header.isNullOrBlank() && !body.isNullOrBlank())
                        decryptedTopics.add(TopicDTO(
                            creationDate = topics[j].creationDate,
                            creatorID = topics[j].creatorID,
                            approved = topics[j].approved,
                            header= header,
                            body = body,
                            imageURL = imageURL,
                            id = topics[j].id,
                            communityID = topics[j].communityID,
                            mainTopicID = topics[j].mainTopicID
                        ))
                    if(!topicVisualizationDAO.check(topicID = topics[j].id, userID = requester))
                        topicVisualizationDAO.create(topicID = topics[j].id, userID = requester)
                }
                timeline.addAll(decryptedTopics)
            }
            timeline
        }catch(e: Exception){
            listOf()
        }
    }
    suspend fun readNewItemsTimeline(requester: String, topicDAO: TI, userSocialDAO: SI, decryption: AESInterface, topicVisualizationDAO: TVI): List<TopicDTO>{
        return try{
            val timePeriod = System.currentTimeMillis() - 259200000
            val following = userSocialDAO.readAll(requester, following = true)
            val timeline = mutableListOf<TopicDTO>()
            for(i in following.indices){
                val topics = topicDAO.readByTimePeriod(following[i].followedID, timePeriod,false)
                val decryptedTopics= mutableListOf<TopicDTO>()
                for(j in topics.indices){
                    if(!topicVisualizationDAO.check(topicID = topics[j].id, userID = requester)){
                        val header =decryption.decrypt(myKey = key, data = topics[j].header)
                        val body = decryption.decrypt(myKey = key, data = topics[j].body)
                        val imageURL = topics[j].imageURL?.let { decryption.decrypt(myKey = key, data = it) }
                        if(!header.isNullOrBlank() && !body.isNullOrBlank())
                            decryptedTopics.add(TopicDTO(
                                creationDate = topics[j].creationDate,
                                creatorID = topics[j].creatorID,
                                approved = topics[j].approved,
                                header= header,
                                body = body,
                                imageURL = imageURL,
                                id = topics[j].id,
                                communityID = topics[j].communityID,
                                mainTopicID = topics[j].mainTopicID
                            ))
                        topicVisualizationDAO.create(topicID = topics[j].id, userID = requester)
                    }
                }
                timeline.addAll(decryptedTopics)
            }
            timeline
        }catch(e: Exception){
            listOf()
        }
    }


    suspend fun readSubjectTopics(requester: String, community: Boolean, subjectID: String, topicDAO: TI, decryption: AESInterface, topicVisualizationDAO: TVI, timePeriod: Long):List<TopicDTO>{
        return try{
                 val topics = when(timePeriod <= 0){
                     true-> topicDAO.readBySubject(subjectID, community = community)
                     false-> topicDAO.readByTimePeriod(subjectID, until=timePeriod, community = community)
                 }
                 val decryptedTopics = mutableListOf<TopicDTO>()

                 for(j in topics.indices) {
                     if (!topicVisualizationDAO.check(topicID = topics[j].id, userID = requester)) {
                         val header = decryption.decrypt(myKey = key, data = topics[j].header)
                         val body = decryption.decrypt(myKey = key, data = topics[j].body)
                         val imageURL = topics[j].imageURL?.let { decryption.decrypt(myKey = key, data = it) }
                         if (!header.isNullOrBlank() && !body.isNullOrBlank())
                             decryptedTopics.add(
                                 TopicDTO(
                                     creationDate = topics[j].creationDate,
                                     creatorID = topics[j].creatorID,
                                     approved = topics[j].approved,
                                     header = header,
                                     body = body,
                                     imageURL = imageURL,
                                     id = topics[j].id,
                                     communityID = topics[j].communityID,
                                     mainTopicID = topics[j].mainTopicID
                                 )
                             )
                         topicVisualizationDAO.create(topicID = topics[j].id, userID = requester)
                     }
                     if(subjectID != requester && !topicVisualizationDAO.check(topicID = topics[j].id, userID = requester))
                         topicVisualizationDAO.create(topicID = topics[j].id, userID = requester)
                 }

                 decryptedTopics
        }catch(e: Exception){
            listOf()
        }
    }
}