package br.meetingplace.server.modules.community.service.factory

import br.meetingplace.server.modules.community.dao.CI
import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.entitie.Community
import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.community.CommunityCreationDTO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object CommunityFactory {

    fun create(data: CommunityCreationDTO, communityDAO: CI): Status {
        return try {
            if(transaction { !User.select { User.id eq data.userID }.empty() }){
                communityDAO.create(data)
            }
            else Status(404, StatusMessages.NOT_FOUND)
        }catch (e: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}