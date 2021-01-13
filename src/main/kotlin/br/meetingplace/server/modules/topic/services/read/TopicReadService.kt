package br.meetingplace.server.modules.topic.services.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.topic.dao.seen.TVI
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dto.response.TopicDataDTO
import br.meetingplace.server.modules.topic.key.AESTopicKey
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI

object TopicReadService {
    private const val key = AESTopicKey.key

    suspend fun readAllTimeline(requester: String, userDAO: UI, communityDAO: CI, topicDAO: TI, userSocialDAO: SI, decryption: AESInterface, topicStatusDAO: TVI): List<TopicDataDTO>{
        return try{
            val timePeriod = System.currentTimeMillis() - 259200000
            val following = userSocialDAO.readAll(requester, following = true)
            val timeline = mutableListOf<TopicDataDTO>()
            for(i in following.indices){
                val topics = topicDAO.readByTimePeriod(following[i].followedID, timePeriod, false)
                val decryptedTopics= mutableListOf<TopicDataDTO>()
                for(j in topics.indices){
                    val header =decryption.decrypt(myKey = key, data = topics[j].header)
                    val body = decryption.decrypt(myKey = key, data = topics[j].body)
//                    val imageURL = topics[j].imageURL?.let { decryption.decrypt(myKey = key, data = it) }
                    val user = userDAO.readByID(topics[j].creatorID)
                    val communityEntity = topics[j].communityID?.let { communityDAO.read(it) }
                    if(!header.isNullOrBlank() && !body.isNullOrBlank() && user != null){
                        decryptedTopics.add(TopicDataDTO(
                            creationDate = topics[j].creationDate,
                            creatorID = topics[j].creatorID,
                            approved = topics[j].approved,
                            header= header,
                            body = body,
                            imageURL = topics[j].imageURL,
                            id = topics[j].id,
                            communityID = topics[j].communityID,
                            mainTopicID = topics[j].mainTopicID,
                            subjectImageURL = user.imageURL,
                            subjectName = user.name,
                            communityName = communityEntity?.name
                        ))
                        if(!topicStatusDAO.check(topicID = topics[j].id, userID = requester))
                            topicStatusDAO.create(topicID = topics[j].id, userID = requester)
                    }
                }
                timeline.addAll((decryptedTopics.toList()).sortedBy { it.creationDate } .reversed())
            }
            timeline
        }catch(e: Exception){
            listOf()
        }
    }
    suspend fun readNewItemsTimeline(requester: String, communityDAO: CI, userDAO: UI, topicDAO: TI, userSocialDAO: SI, decryption: AESInterface, topicStatusDAO: TVI): List<TopicDataDTO>{
        return try{
            val timePeriod = System.currentTimeMillis() - 259200000
            val following = userSocialDAO.readAll(requester, following = true)
            val timeline = mutableListOf<TopicDataDTO>()
            for(i in following.indices){
                val topics = topicDAO.readByTimePeriod(following[i].followedID, timePeriod,false)
                val decryptedTopics= mutableListOf<TopicDataDTO>()
                for(j in topics.indices){
                    if(!topicStatusDAO.check(topicID = topics[j].id, userID = requester)){
                        val header =decryption.decrypt(myKey = key, data = topics[j].header)
                        val body = decryption.decrypt(myKey = key, data = topics[j].body)
                        //val imageURL = topics[j].imageURL?.let { decryption.decrypt(myKey = key, data = it) }
                        val user = userDAO.readByID(topics[j].creatorID)
                        val communityEntity = topics[j].communityID?.let { communityDAO.read(it) }
                        if(!header.isNullOrBlank() && !body.isNullOrBlank() && user != null){
                            decryptedTopics.add(TopicDataDTO(
                                creationDate = topics[j].creationDate,
                                creatorID = topics[j].creatorID,
                                approved = topics[j].approved,
                                header= header,
                                body = body,
                                imageURL = topics[j].imageURL,
                                id = topics[j].id,
                                communityID = topics[j].communityID,
                                mainTopicID = topics[j].mainTopicID,
                                subjectImageURL = user.imageURL,
                                subjectName = user.name,
                                communityName = communityEntity?.name
                            ))
                            topicStatusDAO.create(topicID = topics[j].id, userID = requester)
                        }
                    }
                }
                timeline.addAll((decryptedTopics.toList()).sortedBy { it.creationDate } .reversed())
            }
            timeline
        }catch(e: Exception){
            listOf()
        }
    }


    suspend fun readSubjectTopics(requester: String, communityDAO: CI, userDAO: UI, community: Boolean, subjectID: String, topicDAO: TI, decryption: AESInterface):List<TopicDataDTO>{
        return try{
             val topics = topicDAO.readByTimePeriod(subjectID, since = System.currentTimeMillis() - 604800000, community = community)
             val decryptedTopics = mutableListOf<TopicDataDTO>()
             for(j in topics.indices) {
                 val header = decryption.decrypt(myKey = key, data = topics[j].header)
                 val body = decryption.decrypt(myKey = key, data = topics[j].body)
                 //val imageURL = topics[j].imageURL?.let { decryption.decrypt(myKey = key, data = it) }
                 val user = userDAO.readByID(topics[j].creatorID)
                 val communityEntity = topics[j].communityID?.let { communityDAO.read(it) }
                 if (!header.isNullOrBlank() && !body.isNullOrBlank() && user != null){
                     decryptedTopics.add(
                         TopicDataDTO(
                             creationDate = topics[j].creationDate,
                             creatorID = topics[j].creatorID,
                             approved = topics[j].approved,
                             header = header,
                             body = body,
                             imageURL = topics[j].imageURL,
                             id = topics[j].id,
                             communityID = topics[j].communityID,
                             mainTopicID = topics[j].mainTopicID,
                             subjectName = user.name,
                             subjectImageURL = user.imageURL,
                             communityName = communityEntity?.name
                         )
                     )
                 }
             }
            (decryptedTopics.toList()).sortedBy { it.creationDate } .reversed()
        }catch(e: Exception){
            listOf()
        }
    }
}