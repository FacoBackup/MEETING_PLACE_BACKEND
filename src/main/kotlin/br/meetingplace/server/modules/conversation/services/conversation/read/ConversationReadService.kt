package br.meetingplace.server.modules.conversation.services.conversation.read

import br.meetingplace.server.methods.AESInterface
import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dao.conversation.owners.COI
import br.meetingplace.server.modules.conversation.dao.messages.MI
import br.meetingplace.server.modules.conversation.dao.messages.status.MSI
import br.meetingplace.server.modules.conversation.dto.response.conversation.ConversationFullDTO
import br.meetingplace.server.modules.conversation.key.AESMessageKey
import br.meetingplace.server.modules.user.dao.user.UI

object ConversationReadService {

    suspend fun readConversations(
        requester: Long,
        conversationMemberDAO: CMI,
        conversationDAO: CI, userDAO: UI,
        conversationOwnerDAO: COI,
        messageStatusDAO: MSI,
        messageDAO: MI,
        decryption: AESInterface
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
                                members = conversationMemberDAO.readAllByConversation(conversationID = private[i].conversationID),
                                unreadMessages = unreadMessages,
                                userName = simplifiedUser.name,
                                latestMessage = conversationData.latestMessage,
                                latestMessageContent = messageDAO.readLastMessage(conversationID = private[i].conversationID, userID = requester)?.let {
                                    decryption.decrypt(myKey = AESMessageKey.key ,
                                        it
                                    )
                                }
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
                                latestMessage = conversationData.latestMessage,
                                latestMessageContent = messageDAO.readLastMessage(conversationID = memberIn[i].conversationID, userID = requester)?.let {
                                    decryption.decrypt(myKey = AESMessageKey.key ,
                                        it
                                    )
                                }
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