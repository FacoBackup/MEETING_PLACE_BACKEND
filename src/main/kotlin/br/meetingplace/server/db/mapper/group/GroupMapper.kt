package br.meetingplace.server.db.mapper.group

import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.db.Group
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.db.GroupMember
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dto.GroupDTO
import br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dto.GroupMembersDTO
import org.jetbrains.exposed.sql.ResultRow

object GroupMapper: GroupMapperInterface {
    override fun mapGroup(it: ResultRow): GroupDTO {
        return GroupDTO(communityID = it[Group.communityID], id = it[Group.id], name = it[Group.name], imageURL = it[Group.imageURL], creationDate = it[Group.creationDate].toString("dd-MM-yyyy"), about = it[Group.about], approved = it[Group.approved])
    }

    override fun mapGroupMembers(it: ResultRow): GroupMembersDTO {
        return GroupMembersDTO(groupID = it[GroupMember.groupID], admin = it[GroupMember.admin], userID = it[GroupMember.userID])
    }
}