package br.meetingplace.server.modules.groups.dao.factory

import br.meetingplace.server.db.mapper.community.CommunityMapperInterface
import br.meetingplace.server.db.mapper.group.GroupMapperInterface
import br.meetingplace.server.modules.chat.db.Chat
import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.groups.db.Group
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.data.CreationData
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

object GroupFactory {

    fun create(data: CreationData, communityMemberMapper: CommunityMapperInterface, groupMapper: GroupMapperInterface) : Status {
        return try {
            when(data.communityID.isNullOrBlank()){
                true->{ //user
                    if(!User.select {User.id eq data.userID}.empty()){
                        val groupID = UUID.randomUUID().toString()
                        Group.insert {
                            it[id] = groupID
                            it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                            it[communityID] = null
                            it[imageURL] = data.imageURL
                            it[approved] = true
                            it[about] = data.about
                            it[name] = data.name
                        }
                        Chat.insert {
                            it[id] = UUID.randomUUID().toString()
                            it[this.groupID] = groupID
                            it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                        }
                    }
                }
                false->{ //community
                    val member = CommunityMember.select { CommunityMember.userID eq data.userID }.map { communityMemberMapper.mapCommunityMembersDTO(it)}.firstOrNull()
                    if(!User.select {User.id eq data.userID}.empty() && !Community.select { Community.id eq data.communityID}.empty() && member != null){
                        Group.insert {
                            it[id] = UUID.randomUUID().toString()
                            it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                            it[communityID] = data.communityID
                            it[imageURL] = data.imageURL
                            it[approved] = member.admin
                            it[about] = data.about
                            it[name] = data.name
                        }
                        Chat.insert {
                            it[id] = UUID.randomUUID().toString()
                            it[this.groupID] = groupID
                            it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                        }
                    }
                }
            }
            Status(200, StatusMessages.OK)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

}