package br.meetingplace.server.modules.conversation.services.conversation.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.ConversationDAO
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationFullDTO
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.user.dao.user.UI

object ConversationReadService {
    suspend fun updateConversationInfo( requester: Long,
                                        conversationID: Long,
                                        conversationMemberDAO: CMI,
                                        conversationDAO: CI, userDAO: UI,
                                        conversationOwnerDAO: COI,
                                        messageStatusDAO: MSI){
        TODO()
    }
    suspend fun readNewestConversations(
        requester: Long,
        minID: Long,
        conversationMemberDAO: CMI,
        conversationDAO: CI, userDAO: UI,
        conversationOwnerDAO: COI,
        messageStatusDAO: MSI
    ): List<ConversationFullDTO> {

        return try {
            val conversations = conversationDAO.readNewest(minID = minID, requester= requester)
            val fullConversationData = mutableListOf<ConversationFullDTO>()

                for(i in conversations.indices){
                    val unreadMessages = messageStatusDAO.unseenMessagesCount(conversationID = conversations[i].id, userID = requester)

                    when(conversations[i].isGroup){
                        true->{
                            fullConversationData.add(
                                ConversationFullDTO(
                                    id = conversations[i].id,
                                    name = conversations[i].name,
                                    about = conversations[i].about,
                                    imageURL = conversations[i].imageURL,
                                    isGroup = conversations[i].isGroup,
                                    creationDate = conversations[i].creationDate,
                                    members = conversationMemberDAO.readAllByConversation(conversationID = conversations[i].id),
                                    unreadMessages =unreadMessages,
                                    userName = null,
                                    userID = null,
                                    latestMessage = conversations[i].latestMessage,
                                )
                            )
                        }
                        false->{
                            val owners = conversationOwnerDAO.readByConversation(conversations[i].id)
                            if (owners != null){
                                val simplifiedUser = userDAO.readSimplifiedUserByID(if(owners.primaryUserID != requester) owners.primaryUserID else owners.secondaryUserID)
                                if(simplifiedUser != null) {
                                    fullConversationData.add(
                                        ConversationFullDTO(
                                            id = conversations[i].id,
                                            name = conversations[i].name,
                                            about = conversations[i].about,
                                            imageURL =simplifiedUser.imageURL,
                                            isGroup = conversations[i].isGroup,
                                            creationDate = conversations[i].creationDate,
                                            members = listOf(),
                                            unreadMessages = unreadMessages,
                                            userName = simplifiedUser.name,
                                            userID= simplifiedUser.userID,
                                            latestMessage = conversations[i].latestMessage,
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            (fullConversationData.toList()).sortedBy { it.latestMessage?: it.unreadMessages }

        }catch (e: Exception){
            listOf()
        }
    }
    suspend fun readConversations(
        requester: Long,
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
                    val unreadMessages = messageStatusDAO.unseenMessagesCount(conversationID = private[i].conversationID, userID = requester)
                    val conversationData = conversationDAO.read(private[i].conversationID)

                    val simplifiedUser = userDAO.readSimplifiedUserByID(if(private[i].primaryUserID != requester) private[i].primaryUserID else private[i].secondaryUserID)
                    if(conversationData != null && simplifiedUser != null) {
                        conversations.add(
                            ConversationFullDTO(
                                id = conversationData.id,
                                name = conversationData.name,
                                about = conversationData.about,
                                imageURL =simplifiedUser.imageURL,
                                isGroup = conversationData.isGroup,
                                creationDate = conversationData.creationDate,
                                members = listOf(),
                                unreadMessages = unreadMessages,
                                userName = simplifiedUser.name,
                                userID= simplifiedUser.userID,
                                latestMessage = conversationData.latestMessage,
                            )
                        )
                    }
                }
                for (i in memberIn.indices){
                    val conversationData = conversationDAO.read(memberIn[i].conversationID)
                    val unreadMessages = messageStatusDAO.unseenMessagesCount(conversationID = memberIn[i].conversationID, userID = requester)
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
                                unreadMessages =unreadMessages,
                                userName = null,
                                userID = null,
                                latestMessage = conversationData.latestMessage,
                        )
                        )
                    }

                }
            }

            (conversations.toList()).sortedBy { it.latestMessage?: it.unreadMessages }

        }catch (e: Exception){
            listOf()
        }
    }
}