package br.meetingplace.server.modules.user.dao.search

import br.meetingplace.server.db.interfaces.TopicDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.routers.generic.requests.Login

interface UserReaderInterface {
    fun getMyTopics(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface): MutableList<Topic>
    fun getMyTimeline(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface): MutableList<Topic>
}