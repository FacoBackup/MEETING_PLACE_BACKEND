package br.meetingplace.server.modules.search.dao

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.search.dto.SimplifiedUser
import br.meetingplace.server.requests.generic.operators.SimpleOperator


class UserSearch private constructor() {
    companion object {
        private val Class = UserSearch()
        fun getClass() = Class
    }

    fun searchUser(data: SimpleOperator, rwUser: UserDBInterface): SimplifiedUser?{
        val user = rwUser.select(data.identifier.ID)
        return if (user != null) {
           SimplifiedUser(user.getUserName(), imageURL = user.getImageURL(), user.getEmail())
        } else null
    }
}