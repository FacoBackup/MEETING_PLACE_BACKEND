package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.db.community.CommunityDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.requests.generic.SimpleOperator

interface SocialInterface {
    fun follow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface)
    fun unfollow(data: SimpleOperator, rwUser: UserDBInterface, rwCommunity: CommunityDBInterface)
}