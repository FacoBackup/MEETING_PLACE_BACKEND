package br.meetingplace.server.services.community.classes.dependencies.groups

interface CommunityGroupsInterface {
    fun getApprovedGroups(): List<String>
    fun getGroupsInValidation(): List<String>
    fun checkGroupApproval(id: String): Boolean
    fun removeApprovedGroup(group: String)
    fun updateGroupsInValidation(group: String, approve: Boolean?)
}