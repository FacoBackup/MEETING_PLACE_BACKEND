package br.meetingplace.server.modules.group.service.factory

import br.meetingplace.server.modules.community.dao.CommunityDAOInterface
import br.meetingplace.server.modules.community.entitie.Community
import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.group.entitie.Group
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object GroupFactoryDAO {

    fun create(data: GroupCreationDTO, communityMapper: CommunityDAOInterface) : Status {
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