package br.meetingplace.server.modules.group.dao.factory

import br.meetingplace.server.db.mapper.community.CommunityMapperInterface
import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.group.db.Group
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.requests.group.RequestGroupCreation
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

object GroupFactoryDAO {

    fun create(data: RequestGroupCreation, communityMapper: CommunityMapperInterface) : Status {
        return try {
            when(data.communityID.isNullOrBlank()){
                true->{ //user
                    if(transaction { !User.select {User.id eq data.userID}.empty() })
                        transaction {
                            Group.insert {
                                it[id] = UUID.randomUUID().toString()
                                it[creationDate] = DateTime.now()
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
                                it[creationDate] = DateTime.now()
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