package br.meetingplace.server.modules.user.dao.delete

import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.data.Login

interface UserDeleteInterface {
    fun delete(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface)
}