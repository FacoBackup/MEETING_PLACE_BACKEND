package br.meetingplace.server.modules.conversation.services.message.read

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dao.notification.MNI
import br.meetingplace.server.modules.conversation.dto.response.notification.MessageNotificationDTO
import br.meetingplace.server.modules.user.dao.user.UI

object MessageNotificationReadService {
    suspend fun readByPage(requester: String, page: Long, messageNotificationDAO: MNI, userDAO: UI,conversationOwnersDAO: COI, conversationDAO: CI): List<MessageNotificationDTO>{
        return try {
            val notifications = messageNotificationDAO.read(requester, page)
            val response = mutableListOf<MessageNotificationDTO>()
            for (i in notifications.indices){
                val isUser = conversationOwnersDAO.readByConversation(notifications[i].conversationID)

                if(isUser != null){
                    val user = userDAO.readByID(if(isUser.primaryUserID != requester) isUser.primaryUserID else isUser.secondaryUserID)
                    if(user != null)
                        response.add(
                            MessageNotificationDTO(
                                subjectImageURL = user.imageURL,
                                subjectID = user.email,
                                subjectName = user.name,
                                isGroup = false,
                                page = notifications[i].page,
                                creationDate = notifications[i].creationDate,
                                conversationID = notifications[i].conversationID,
                                messageID = notifications[i].messageID,
                                seenAt = notifications[i].seenAt
                            ))

                    else{
                        val group = conversationDAO.read(notifications[i].conversationID)
                        if(group != null)
                            response.add(
                                MessageNotificationDTO(
                                    subjectImageURL = group.imageURL,
                                    subjectID = null,
                                    subjectName = group.name,
                                    isGroup = true,
                                    page = notifications[i].page,
                                    creationDate = notifications[i].creationDate,
                                    conversationID = notifications[i].conversationID,
                                    messageID = notifications[i].messageID,
                                    seenAt = notifications[i].seenAt
                                ))
                    }
                }

            }
            response
        }catch (e: Exception){
            listOf()
        }
    }

    suspend fun readLatestNotifications (requester: String, messageNotificationDAO: MNI, userDAO: UI, conversationDAO: CI, conversationOwnersDAO: COI): List<MessageNotificationDTO>{
        return try {
            val notifications = messageNotificationDAO.readLastPage(requester)
            val response = mutableListOf<MessageNotificationDTO>()
            for (i in notifications.indices){
                val isUser = conversationOwnersDAO.readByConversation(notifications[i].conversationID)

                if(isUser != null){
                    val user = userDAO.readByID(if(isUser.primaryUserID != requester) isUser.primaryUserID else isUser.secondaryUserID)
                    if(user != null)
                        response.add(
                            MessageNotificationDTO(
                                subjectImageURL = user.imageURL,
                                subjectID = user.email,
                                subjectName = user.name,
                                isGroup = false,
                                page = notifications[i].page,
                                creationDate = notifications[i].creationDate,
                                conversationID = notifications[i].conversationID,
                                messageID = notifications[i].messageID,
                                seenAt = notifications[i].seenAt
                            ))

                    else{
                        val group = conversationDAO.read(notifications[i].conversationID)
                        if(group != null)
                            response.add(
                                MessageNotificationDTO(
                                    subjectImageURL = group.imageURL,
                                    subjectID = null,
                                    subjectName = group.name,
                                    isGroup = true,
                                    page = notifications[i].page,
                                    creationDate = notifications[i].creationDate,
                                    conversationID = notifications[i].conversationID,
                                    messageID = notifications[i].messageID,
                                    seenAt = notifications[i].seenAt
                                ))
                    }
                }

                }
            response
        }catch (e: Exception){
            listOf()
        }
    }
}