package br.meetingplace.server.modules.conversation.services.conversation.read

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationFullDTO
import br.meetingplace.server.modules.user.dao.user.UI

object ConversationReadService {

    suspend fun readConversations(
        requester: String,
        conversationMemberDAO: CMI,
        conversationDAO: CI, userDAO: UI,
        conversationOwnerDAO: COI,
        messageStatusDAO: MSI
        ): List<ConversationFullDTO> {

        return try {
            val conversations = mutableListOf<ConversationFullDTO>()
            if(userDAO.check(requester)){
                val memberIn = conversationMemberDAO.readAllByUser(requester)
                val private = conversationOwnerDAO.readAll(requester)
                for(i in private.indices){
                    val unreadMessages = messageStatusDAO.unseenMessages(conversationID = private[i].conversationID, userID = requester)
                    val conversationData = conversationDAO.read(private[i].conversationID)
                    if(conversationData != null) {
                        conversations.add(
                            ConversationFullDTO(
                                id = conversationData.id,
                                name = conversationData.name,
                                about = conversationData.about,
                                imageURL = conversationData.imageURL,
                                isGroup = conversationData.isGroup,
                                creationDate = conversationData.creationDate,
                                members = conversationMemberDAO.readAllByConversation(conversationID = private[i].conversationID),
                                unreadMessages = unreadMessages
                            )
                        )
                    }
                }
                for (i in memberIn.indices){
                    val conversationData = conversationDAO.read(memberIn[i].conversationID)
                    val unreadMessages = messageStatusDAO.unseenMessages(conversationID = memberIn[i].conversationID, userID = requester)
                    if(conversationData != null){
                        conversations.add(
                            ConversationFullDTO(
                                id = conversationData.id,
                                name = conversationData.name,
                                about = conversationData.about,
                                imageURL = conversationData.imageURL,
                                isGroup = conversationData.isGroup,
                                creationDate = conversationData.creationDate,
                                members = conversationMemberDAO.readAllByConversation(conversationID = memberIn[i].conversationID),
                                unreadMessages =unreadMessages
                        )
                        )
                    }

                }
            }
            conversations
        }catch (e: Exception){
            listOf()
        }
    }
}