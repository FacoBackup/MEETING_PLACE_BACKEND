package br.meetingplace.server.modules.user.dao.TODO

import br.meetingplace.server.db.topic.TopicDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.topic.dto.Topic
import br.meetingplace.server.requests.generic.data.Login

interface UserReaderInterface {
    fun getMyTopics(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface): MutableList<Topic>
    fun getMyTimeline(data: Login, rwUser: UserDBInterface, rwTopic: TopicDBInterface): MutableList<Topic>
}