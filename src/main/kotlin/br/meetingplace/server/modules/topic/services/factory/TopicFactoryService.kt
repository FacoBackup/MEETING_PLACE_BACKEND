package br.meetingplace.server.modules.topic.services.factory

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.community.dto.MemberType
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import br.meetingplace.server.modules.topic.key.AESTopicKey
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object TopicFactoryService {
    private const val key = AESTopicKey.key
    suspend fun create(requester: String, data: RequestTopicCreation, topicDAO: TI, userDAO: UI, communityMemberDAO: CMI, encryption: AESInterface): HttpStatusCode {
        return try {
            val user = userDAO.readByID(requester)
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    println("STEP 1")
                    return if (user != null && data.mainTopicID.isNullOrBlank()) {
                        val encryptedHeader = encryption.encrypt(myKey = key, data.header)
                        val encryptedBody= encryption.encrypt(myKey = key, data.body)

                        if(encryptedBody.isNullOrBlank() || encryptedHeader.isNullOrBlank())
                            return HttpStatusCode.InternalServerError
                        else topicDAO.create(
                            header = encryptedHeader,
                            body = encryptedBody,
                            imageURL = data.imageURL ,
                            communityID = null,
                            userID = requester,
                            mainTopicID = null,
                            approved = true,
                            userName = user.name)
                    } else HttpStatusCode.FailedDependency
                }
                false -> {
                    val member = communityMemberDAO.read(data.communityID, userID = requester)
                    return if (user != null && member != null && data.mainTopicID.isNullOrBlank()) {
                        val encryptedHeader = encryption.encrypt(myKey = key, data.header)
                        val encryptedBody= encryption.encrypt(myKey = key, data.body)

                        if(encryptedBody.isNullOrBlank() || encryptedHeader.isNullOrBlank())
                            return HttpStatusCode.InternalServerError
                        else topicDAO.create(
                            header = encryptedHeader,
                            body = encryptedBody,
                            imageURL = data.imageURL ,
                            communityID = member.communityID,
                            userID = requester,
                            mainTopicID = null,
                            approved = member.role == MemberType.MODERATOR.toString(),
                            userName = user.name)
                    } else HttpStatusCode.FailedDependency
                }
            }
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }

    suspend fun createComment(requester: String, data: RequestTopicCreation, topicDAO: TI, userDAO: UI, communityMemberDAO: CMI, encryption: AESInterface): HttpStatusCode {
        return try {
            val user = userDAO.readByID(requester)
            when (data.communityID.isNullOrBlank()) {
                true -> {
                    return if (user != null && !data.mainTopicID.isNullOrBlank() && topicDAO.check(data.mainTopicID)) {
                        val encryptedHeader = encryption.encrypt(myKey = key, data.header)
                        val encryptedBody= encryption.encrypt(myKey = key, data.body)
                        val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                        if(encryptedBody.isNullOrBlank() || encryptedHeader.isNullOrBlank())
                            return HttpStatusCode.InternalServerError
                        else topicDAO.create(
                            header = encryptedHeader,
                            body = encryptedBody,
                            imageURL = encryptedImageURL ,
                            communityID = null,
                            userID = requester,
                            mainTopicID = data.mainTopicID,
                            approved = true,
                            userName = user.name)
                    } else HttpStatusCode.FailedDependency
                }
                false -> {
                    val member = communityMemberDAO.read(data.communityID, userID = requester)
                    val mainTopic = data.mainTopicID?.let { topicDAO.read(it) }
                    return if (mainTopic  != null && mainTopic.approved  && user != null && member != null) {

                        val encryptedHeader = encryption.encrypt(myKey = key, data.header)
                        val encryptedBody= encryption.encrypt(myKey = key, data.body)
                        val encryptedImageURL = data.imageURL?.let { encryption.encrypt(myKey = key, it) }
                        if(encryptedBody.isNullOrBlank() || encryptedHeader.isNullOrBlank())
                            return HttpStatusCode.InternalServerError
                        else topicDAO.create(
                            header = encryptedHeader,
                            body = encryptedBody,
                            imageURL = encryptedImageURL ,
                            communityID = member.communityID,
                            userID = requester,
                            mainTopicID = data.mainTopicID,
                            approved = member.role == MemberType.MODERATOR.toString(),
                            userName = user.name)
                    } else HttpStatusCode.FailedDependency
                }
            }
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}
