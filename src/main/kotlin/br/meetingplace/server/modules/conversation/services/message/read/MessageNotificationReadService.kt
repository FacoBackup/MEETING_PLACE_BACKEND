package br.meetingplace.server.modules.conversation.services.message.read

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dao.notification.MNI
import br.meetingplace.server.modules.conversation.dto.response.notification.MessageNotificationDTO
import br.meetingplace.server.modules.user.dao.user.UI

object MessageNotificationReadService {
    suspend fun readByPage(requester: String, page: Long, messageNotificationDAO: MNI, userDAO: UI, conversationDAO: CI): List<MessageNotificationDTO>{
        return try {
            val notifications = messageNotificationDAO.read(requester, page)
            val response = mutableListOf<MessageNotificationDTO>()
            for (i in notifications.indices){
                val subjectAsGroup = conversationDAO.read(notifications[i].subjectID)
                val subjectAsUser = userDAO.readByID(notifications[i].subjectID)
                if(subjectAsGroup != null)
                    response.add(
                        MessageNotificationDTO(
                            subjectImageURL = subjectAsGroup.imageURL,
                            subjectID = notifications[i].subjectID,
                            subjectName = subjectAsGroup.name,
                            isGroup = true,
                            page = notifications[i].page,
                            creationDate = notifications[i].creationDate
                        ))
                else if(subjectAsUser != null)
                    response.add(
                        MessageNotificationDTO(
                            subjectImageURL = subjectAsUser.imageURL,
                            subjectID = notifications[i].subjectID,
                            subjectName = subjectAsUser.name,
                            isGroup = false,
                            page = notifications[i].page,
                            creationDate = notifications[i].creationDate
                        ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readLatestNotifications (requester: String, messageNotificationDAO: MNI, userDAO: UI, conversationDAO: CI): List<MessageNotificationDTO>{
        return try {
            val notifications = messageNotificationDAO.readLastPage(requester)
            val response = mutableListOf<MessageNotificationDTO>()
            for (i in notifications.indices){
                val subjectAsGroup = conversationDAO.read(notifications[i].subjectID)
                val subjectAsUser = userDAO.readByID(notifications[i].subjectID)
                if(subjectAsGroup != null)
                    response.add(
                        MessageNotificationDTO(
                            subjectImageURL = subjectAsGroup.imageURL,
                            subjectID = notifications[i].subjectID,
                            subjectName = subjectAsGroup.name,
                            isGroup = true,
                            page = notifications[i].page,
                            creationDate = notifications[i].creationDate
                        ))
                else if(subjectAsUser != null)
                    response.add(
                        MessageNotificationDTO(
                            subjectImageURL = subjectAsUser.imageURL,
                            subjectID = notifications[i].subjectID,
                            subjectName = subjectAsUser.name,
                            isGroup = false,
                            page = notifications[i].page,
                            creationDate = notifications[i].creationDate
                        ))
            }
            response
        }catch (e: Exception){
            listOf()
        }
    }
}