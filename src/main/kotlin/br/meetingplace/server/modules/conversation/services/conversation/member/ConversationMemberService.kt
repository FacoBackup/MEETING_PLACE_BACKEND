package br.meetingplace.server.modules.conversation.services.conversation.member

import br.meetingplace.server.modules.conversation.dao.conversation.CI
import br.meetingplace.server.modules.conversation.dao.conversation.member.CMI
import br.meetingplace.server.modules.conversation.dto.requests.conversation.RequestConversationMember
import io.ktor.http.*

object ConversationMemberService {
    fun addMember(requester: String, data: RequestConversationMember, conversationMemberDAO: CMI, conversationDAO: CI): HttpStatusCode {
        return try{
            val conversation = conversationDAO.read(data.conversationID)
            if(conversation != null && conversation.isGroup && conversationMemberDAO.check(userID = requester, conversationID = data.conversationID) &&
                !conversationMemberDAO.check(userID = data.memberID, conversationID = data.conversationID))
                conversationMemberDAO.create(data.memberID, conversationID = data.conversationID, false)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

    fun removeMember(requester: String, data: RequestConversationMember, conversationMemberDAO: CMI, conversationDAO: CI): HttpStatusCode {
        return try {
            val userMember = conversationMemberDAO.read(requester, conversationID = data.conversationID)
            val conversation = conversationDAO.read(data.conversationID)
            if(conversation != null && conversation.isGroup &&userMember != null && userMember.admin)
                conversationMemberDAO.delete(data.memberID, conversationID = data.conversationID)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
    fun promoteMember(requester: String, data: RequestConversationMember, memberDAO: CMI, conversationDAO: CI): HttpStatusCode{
        return try {
            val member = memberDAO.read(requester, conversationID = data.conversationID)
            val conversation = conversationDAO.read(data.conversationID)
            if(conversation != null && conversation.isGroup &&member != null && member.admin)
                memberDAO.update(data.memberID, conversationID = data.conversationID, admin = true)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }
    fun lowerMember(requester: String, data: RequestConversationMember, memberDAO: CMI, conversationDAO: CI): HttpStatusCode{
        return try {
            val member = memberDAO.read(requester, conversationID = data.conversationID)
            val conversation = conversationDAO.read(data.conversationID)
            if(conversation != null && conversation.isGroup && member != null && member.admin)
                memberDAO.update(data.memberID, conversationID = data.conversationID, admin = false)
            else HttpStatusCode.FailedDependency
        }catch (normal: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}