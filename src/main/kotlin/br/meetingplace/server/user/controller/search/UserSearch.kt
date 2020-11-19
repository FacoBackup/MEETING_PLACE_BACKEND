package br.meetingplace.server.user.controller.search

import br.meetingplace.server.loadstore.interfaces.UserLSInterface
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.user.classes.SimplifiedUser


class UserSearch private constructor() {
    companion object {
        private val Class = UserSearch()
        fun getClass() = Class
    }

    fun searchUser(data: SimpleOperator, rwUser: UserLSInterface): List<SimplifiedUser> {
        val user = rwUser.load(data.identifier.ID)
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
