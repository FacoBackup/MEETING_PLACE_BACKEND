package br.meetingplace.server.user.controller.delete

import br.meetingplace.server.loadstore.interfaces.TopicLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.Login

interface UserDeleteInterface {
    fun delete(data: Login, rwUser: UserLSInterface, rwTopic: TopicLSInterface)
}