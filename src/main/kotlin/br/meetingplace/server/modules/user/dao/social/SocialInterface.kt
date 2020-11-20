package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.db.interfaces.CommunityDBInterface
import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.routers.generic.requests.SimpleOperator

interface SocialInterface {
    fun follow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface)
    fun unfollow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface)
}