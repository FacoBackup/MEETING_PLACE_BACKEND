package br.meetingplace.server.modules.topic.services.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.topic.dao.seen.TVI
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.key.AESTopicKey
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI

object TopicReadService {
    private const val key = AESTopicKey.key

    suspend fun readAllTimeline(requester: String, userDAO: UI, topicDAO: TI, userSocialDAO: SI, decryption: AESInterface, topicVisualizationDAO: TVI): List<TopicDTO>{
        return try{
            val timePeriod = System.currentTimeMillis() - 259200000
            val following = userSocialDAO.readAll(requester, following = true)
            val timeline = mutableListOf<TopicDTO>()
            for(i in following.indices){
                val topics = topicDAO.readByTimePeriod(following[i].followedID, timePeriod)
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
    suspend fun readNewItemsTimeline(requester: String, userDAO: UI, topicDAO: TI, userSocialDAO: SI, decryption: AESInterface, topicVisualizationDAO: TVI): List<TopicDTO>{
        return try{
            val timePeriod = System.currentTimeMillis() - 259200000
            val following = userSocialDAO.readAll(requester, following = true)
            val timeline = mutableListOf<TopicDTO>()
            for(i in following.indices){
                val topics = topicDAO.readByTimePeriod(following[i].followedID, timePeriod)
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
    suspend fun readUserTopics(requester: String, userID: String,userDAO: UI, topicDAO: TI, userSocialDAO: SI, decryption: AESInterface, topicVisualizationDAO: TVI, timePeriod: Long):List<TopicDTO>{
        return try{
             if(userDAO.check(userID))
                 return when(timePeriod <= 0){
                    true->{
                        val topics = topicDAO.readByUser(userID)
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
                            if(userID == requester && !topicVisualizationDAO.check(topicID = topics[j].id, userID = requester))
                                topicVisualizationDAO.create(topicID = topics[j].id, userID = requester)
                        }

                        decryptedTopics
                    }

                    false-> {
                        topicDAO.readByTimePeriod(userID, timePeriod)
                        //TODO
                    }
                }
            else
                listOf()
        }catch(e: Exception){
            listOf()
        }
    }
}