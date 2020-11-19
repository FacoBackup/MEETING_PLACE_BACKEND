package br.meetingplace.server.user.controller.social

import br.meetingplace.server.loadstore.interfaces.CommunityLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.SimpleOperator

interface SocialInterface {
    fun follow(data: SimpleOperator, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface)
    fun unfollow(data: SimpleOperator, rwUser: UserLSInterface, rwCommunity: CommunityLSInterface)
}