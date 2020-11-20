package br.meetingplace.server.modules.user.dto.dependencies.services.group

interface UserGroupsInterface {
    fun updateMyGroups(group: String, delete: Boolean)
    fun updateMemberIn(group: String, leave: Boolean)
    fun getMyGroups(): List<String>
    fun getMemberIn(): List<String>
}