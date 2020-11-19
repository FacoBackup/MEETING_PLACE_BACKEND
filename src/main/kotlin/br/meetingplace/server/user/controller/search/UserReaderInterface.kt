package br.meetingplace.server.user.controller.search

import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.services.topic.classes.Topic

interface UserReaderInterface {
    fun getMyTopics(data: Login, rwUser: UserLSInterface, rwTopic: TopicLSInterface): MutableList<Topic>
    fun getMyTimeline(data: Login, rwUser: UserLSInterface, rwTopic: TopicLSInterface): MutableList<Topic>
}