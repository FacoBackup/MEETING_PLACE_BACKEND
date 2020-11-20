package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.db.interfaces.TopicDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.routers.generic.requests.Login

interface UserDeleteInterface {
    fun delete(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface)
}