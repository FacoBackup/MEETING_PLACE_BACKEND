package br.meetingplace.server.modules.message.services.factory

import br.meetingplace.server.modules.group.dao.GI
import br.meetingplace.server.modules.group.dao.member.GMI
import br.meetingplace.server.modules.message.dao.MI
import br.meetingplace.server.modules.message.dto.requests.RequestMessageCreation
import br.meetingplace.server.modules.user.dao.user.UI


object MessageFactoryService {

    fun createMessage(data: RequestMessageCreation, groupMemberDAO: GMI, userDAO: UI, groupDAO: GI, messageDAO: MI): Status {
        return try {
            if(userDAO.read(data.userID) != null){
                when(data.isGroup){
                    true->{
                        val group =groupDAO.read(data.receiverID)
                        if(group != null && group.approved && groupMemberDAO.read(groupID = data.receiverID, userID = data.userID) != null)
                            messageDAO.create(data)
                        else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
                    }
                    false->{
                        if(userDAO.read(data.receiverID) != null )
                            messageDAO.create(data)
                        else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)

                    }
                }
            }
            else Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

}