package br.meetingplace.server.modules.topic.services.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.topic.dao.archive.TAI
import br.meetingplace.server.modules.topic.dao.opinion.TOI
import br.meetingplace.server.modules.topic.dao.seen.TVI
import br.meetingplace.server.modules.topic.dao.tag.TTI
import br.meetingplace.server.modules.topic.dao.timeline.item.TMII
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dto.response.TopicDTO
import br.meetingplace.server.modules.topic.dto.response.TopicDataDTO
import br.meetingplace.server.modules.topic.key.AESTopicKey
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI

object TopicReadService {
    private const val key = AESTopicKey.key

    suspend fun readTimeline(
        requester: Long,
        maxID: Long?,
        topicOpinionDAO: TOI,
        userDAO: UI,
        communityDAO: CI,
        topicDAO: TI,
        decryption: AESInterface,
        topicArchiveDAO: TAI,
        topicTimelineDAO: TMII,
        topicTagsDAO: TTI,
        topicStatusDAO: TVI): List<TopicDataDTO>{

        return try{
            val topicIDs = if(maxID != null) topicTimelineDAO.readByMaxID(maxID = maxID, userID = requester) else  topicTimelineDAO.readNewest(userID = requester)
            val encryptedTopics = mutableListOf<TopicDTO>()
            for(i in topicIDs.indices){
                val topic = topicDAO.read(topicIDs[i].topicID)
                if(topic != null)
                    encryptedTopics.add(topic)
            }
            val decryptedTopics= mutableListOf<TopicDataDTO>()

            for(j in encryptedTopics.indices){
                val header =decryption.decrypt(myKey = key, data = encryptedTopics[j].header)
                val body = encryptedTopics[j].body?.let { decryption.decrypt(myKey = key, data = it) }

                val user = userDAO.readByID(encryptedTopics[j].creatorID)
                val communityEntity = encryptedTopics[j].communityID?.let { communityDAO.read(it) }
                if(!header.isNullOrBlank() && !body.isNullOrBlank() && user != null){
                    decryptedTopics.add(TopicDataDTO(
                        creationDate = encryptedTopics[j].creationDate,
                        creatorID = encryptedTopics[j].creatorID,
                        approved = encryptedTopics[j].approved,
                        header= header,
                        body = body,
                        imageURL = encryptedTopics[j].imageURL,
                        id = encryptedTopics[j].id,
                        communityID = encryptedTopics[j].communityID,
                        mainTopicID = encryptedTopics[j].mainTopicID,
                        creatorImageURL = user.imageURL,
                        creatorName = user.name,
                        communityName = communityEntity?.name,
                        communityImageURL = communityEntity?.imageURL,
                        comments = topicDAO.readQuantityComments(encryptedTopics[j].id),
                        archived = topicArchiveDAO.check(requester = requester, topicID = encryptedTopics[j].id),
                        liked = topicOpinionDAO.check(userID = requester, topicID = encryptedTopics[j].id, liked = true),
                        disliked = topicOpinionDAO.check(userID = requester, topicID = encryptedTopics[j].id, liked = false)
                    ))
                    if(!topicStatusDAO.check(topicID = encryptedTopics[j].id, userID = requester))
                        topicStatusDAO.create(topicID = encryptedTopics[j].id, userID = requester)
                }
            }

            (decryptedTopics.toList()).sortedBy { it.creationDate } .reversed()

        }catch(e: Exception){
            listOf()
        }
    }

    suspend fun readSubjectTopics(
        requester: Long,
        communityDAO: CI,
        userDAO: UI,
        topicOpinionDAO: TOI,
        community: Boolean,
        subjectID: Long,
        maxID: Long?,
        topicDAO: TI,
        topicTagsDAO: TTI,
        topicArchiveDAO: TAI,
        decryption: AESInterface):List<TopicDataDTO>{

        return try{
             val topics = if(maxID != null ) topicDAO.readByMaxID(subjectID, maxID = maxID, community = community) else topicDAO.readNewestBySubject(subjectID, community = community)
             val decryptedTopics = mutableListOf<TopicDataDTO>()
             for(j in topics.indices) {
                 val header = decryption.decrypt(myKey = key, data = topics[j].header)
                 val body = topics[j].body?.let { decryption.decrypt(myKey = key, data = it) }
                 val user = userDAO.readByID(topics[j].creatorID)
                 val communityEntity = topics[j].communityID?.let { communityDAO.read(it) }
                 if (!header.isNullOrBlank() && !body.isNullOrBlank() && user != null){
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
                         creatorImageURL = user.imageURL,
                         creatorName = user.name,
                         communityName = communityEntity?.name,
                         communityImageURL = communityEntity?.imageURL,
                         comments = topicDAO.readQuantityComments(topics[j].id),
                         archived = topicArchiveDAO.check(requester = requester, topicID = topics[j].id),
                         liked = topicOpinionDAO.check(userID = requester, topicID = topics[j].id, liked = true),
                         disliked = topicOpinionDAO.check(userID = requester, topicID = topics[j].id, liked = false)
                     ))
                 }
             }
            (decryptedTopics.toList()).sortedBy { it.creationDate } .reversed()
        }catch(e: Exception){
            listOf()
        }
    }
}