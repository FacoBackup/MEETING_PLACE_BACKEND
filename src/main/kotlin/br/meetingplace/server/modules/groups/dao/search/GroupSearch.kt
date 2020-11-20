package br.meetingplace.server.modules.groups.dao.search

import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.groups.dto.Group
import br.meetingplace.server.requests.generic.SimpleOperator

class GroupSearch private constructor() {
    companion object {
        private val Class = GroupSearch()
        fun getClass() = Class
    }

    fun seeGroup(data: SimpleOperator, rwUser: UserDBInterface, rwGroup: GroupDBInterface): Group? {
        val user = rwUser.select(data.login.email)

        return if (user != null && data.identifier.owner != null) {
            if (data.identifier.ID in user.getMyGroups()) {
                return rwGroup.select(data.identifier.ID)
            } else if (data.identifier.ID in user.getMemberIn()) {
                return rwGroup.select(data.identifier.ID)
            }
            null
        } else null
    }
}