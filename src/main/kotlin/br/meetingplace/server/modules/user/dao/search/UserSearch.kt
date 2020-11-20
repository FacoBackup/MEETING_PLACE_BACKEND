package br.meetingplace.server.modules.user.dao.search

import br.meetingplace.server.db.user.UserDBInterface
import br.meetingplace.server.modules.user.dto.SimplifiedUser
import br.meetingplace.server.requests.generic.operators.SimpleOperator


class UserSearch private constructor() {
    companion object {
        private val Class = UserSearch()
        fun getClass() = Class
    }

    fun searchUser(data: SimpleOperator, rwUser: UserDBInterface): List<SimplifiedUser> {
        val user = rwUser.select(data.identifier.ID)
        return if (user != null) {
            listOf(SimplifiedUser(user.getUserName(), user.getEmail()))
        } else listOf()
    }
}


//    override fun searchUser(data: Data): List<SimplifiedUser>{
//        when(verifyType(data)){
//            SearchType.BY_NAME->{
//
//            }
//            SearchType.BY_EMAIL->{
//                val user = rw.readUser(data.ID)
//                return if(verify.verifyUser(user)){
//                    listOf(SimplifiedUser(user.getUserName(), user.getEmail(), iDs.attachNameToEmail(user.getUserName(), user.getEmail())))
//                }
//                else listOf()
//            }
//        }
//        return listOf()
//    }
//
//    private fun verifyType(data: Data): SearchType?{
//        return if(data.ID.contains("@"))
//            SearchType.BY_EMAIL
//        else if (!data.ID.contains("@"))
//            SearchType.BY_NAME
//        else null
//    }
