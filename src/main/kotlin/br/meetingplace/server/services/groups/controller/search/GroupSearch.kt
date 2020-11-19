package br.meetingplace.server.services.groups.controller.search

import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.services.groups.classes.Group

class GroupSearch private constructor() {
    companion object {
        private val Class = GroupSearch()
        fun getClass() = Class
    }

    fun seeGroup(data: SimpleOperator, rwUser: UserLSInterface, rwGroup: GroupLSInterface): Group? {
        val user = rwUser.load(data.login.email)

        return if (user != null && data.identifier.owner != null) {
            if (data.identifier.ID in user.getMyGroups()) {
                return rwGroup.read(data.identifier.ID)
            } else if (data.identifier.ID in user.getMemberIn()) {
                return rwGroup.read(data.identifier.ID)
            }
            null
        } else null
    }
}