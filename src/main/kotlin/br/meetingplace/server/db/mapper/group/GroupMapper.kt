package br.meetingplace.server.db.mapper.group

import br.meetingplace.server.modules.groups.db.Group
import br.meetingplace.server.modules.groups.db.GroupMembers
import br.meetingplace.server.modules.groups.dto.GroupDTO
import br.meetingplace.server.modules.groups.dto.GroupMembersDTO
import org.jetbrains.exposed.sql.ResultRow

class GroupMapper: GroupMapperInterface {
    override fun mapGroup(it: ResultRow): GroupDTO {
        return GroupDTO(communityID = it[Group.communityID], id = it[Group.id], name = it[Group.name], imageURL = it[Group.imageURL], creationDate = it[Group.creationDate].toString("dd-MM-yyyy"), about = it[Group.about], approved = it[Group.approved])
    }

    override fun mapGroupMembers(it: ResultRow): GroupMembersDTO {
        return GroupMembersDTO(groupID = it[GroupMembers.groupID], admin = it[GroupMembers.admin], userID = it[GroupMembers.userID])
    }
}