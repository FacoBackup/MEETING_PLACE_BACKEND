package br.meetingplace.server.modules.groups.dao.factory

import br.meetingplace.server.db.mapper.community.CommunityMapperInterface
import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.groups.db.Group
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.generic.RequestCreationData
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

object GroupFactory {

    fun create(data: RequestCreationData, communityMapper: CommunityMapperInterface) : Status {
        return try {
            when(data.communityID.isNullOrBlank()){
                true->{ //user
                    if(transaction { !User.select {User.id eq data.userID}.empty() })
                        transaction {
                            Group.insert {
                                it[id] = UUID.randomUUID().toString()
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                                it[communityID] = null
                                it[imageURL] = data.imageURL
                                it[approved] = true
                                it[about] = data.about
                                it[name] = data.name
                            }
                        }
                }
                false->{ //community
                    val member = transaction { CommunityMember.select { CommunityMember.userID eq data.userID }.map { communityMapper.mapCommunityMembersDTO(it)}.firstOrNull() }
                    if(transaction { !Community.select { Community.id eq data.communityID}.empty() } && member != null)
                        transaction {
                            Group.insert {
                                it[id] = UUID.randomUUID().toString()
                                it[creationDate] = DateTime.parse(LocalDateTime.now().toString("dd-MM-yyyy"))
                                it[communityID] = data.communityID
                                it[imageURL] = data.imageURL
                                it[approved] = member.admin
                                it[about] = data.about
                                it[name] = data.name
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