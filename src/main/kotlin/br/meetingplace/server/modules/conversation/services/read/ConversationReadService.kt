package br.meetingplace.server.modules.conversation.services.read

import br.meetingplace.server.modules.conversation.dao.CI
import br.meetingplace.server.modules.conversation.dao.member.CMI
import br.meetingplace.server.modules.conversation.dao.owners.COI
import br.meetingplace.server.modules.conversation.dto.response.ConversationFullDTO
import br.meetingplace.server.modules.user.dao.user.UI

object ConversationReadService {
    fun readConversation(requester: String, conversationMemberDAO: CMI, conversationDAO: CI, userDAO: UI, conversationOwnerDAO: COI): List<ConversationFullDTO> {
        return try {
            val conversations = mutableListOf<ConversationFullDTO>()
            if(userDAO.check(requester)){
                val memberIn = conversationMemberDAO.readAllByUser(requester)
                val private = conversationOwnerDAO.readAll(requester)
                for(i in private.indices){
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
                                members = conversationMemberDAO.readAllByConversation(conversationID = private[i].conversationID)
                            )
                        )
                    }
                }
                for (i in memberIn.indices){
                    val conversationData = conversationDAO.read(memberIn[i].conversationID)
                    if(conversationData != null){
                        conversations.add(ConversationFullDTO(
                            id = conversationData.id,
                            name = conversationData.name,
                            about = conversationData.about,
                            imageURL = conversationData.imageURL,
                            isGroup = conversationData.isGroup,
                            creationDate = conversationData.creationDate,
                            members = conversationMemberDAO.readAllByConversation(conversationID = memberIn[i].conversationID)
                        ))
                    }

                }
            }
            conversations
        }catch (e: Exception){
            listOf()
        }
    }
}