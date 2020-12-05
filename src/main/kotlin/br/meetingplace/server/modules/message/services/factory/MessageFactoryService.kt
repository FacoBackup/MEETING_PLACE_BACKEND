package br.meetingplace.server.modules.message.services.factory

import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.modules.message.dao.MI
import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*


object MessageFactoryService {

    fun createMessage(data: RequestMessageCreation, groupMemberDAO: GMI, userDAO: UI, groupDAO: GI, messageDAO: MI): HttpStatusCode {
        return try {
            if(userDAO.check(data.userID) == HttpStatusCode.Found)
                when(data.isGroup){
                    true->{
                        val group =groupDAO.read(data.receiverID)
                        if(group != null && group.approved && groupMemberDAO.read(groupID = data.receiverID, userID = data.userID) != null)
                            messageDAO.create(data)
                        else HttpStatusCode.FailedDependency
                    }
                    false->{
                        if(userDAO.check(data.receiverID) == HttpStatusCode.Found )
                            messageDAO.create(data)
                        else HttpStatusCode.FailedDependency

                    }
                }
            else HttpStatusCode.InternalServerError
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }
    }

}