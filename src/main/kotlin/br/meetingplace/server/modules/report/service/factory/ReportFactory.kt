package br.meetingplace.server.modules.report.service.factory

import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.report.entitie.Report
import br.meetingplace.server.modules.topic.entitie.Topic
import br.meetingplace.server.modules.user.entitie.User
import br.meetingplace.server.request.dto.report.ReportCreationDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object ReportFactory{

    fun createReport(data: ReportCreationDTO): Status {
        return try {
            return if(transaction { User.select {User.id eq data.userID} }.firstOrNull() != null &&
                    transaction { CommunityMember.select { (CommunityMember.communityID eq data.communityID) and (CommunityMember.userID eq data.userID)} }.firstOrNull() != null &&
                    transaction { Topic.select { (Topic.id eq data.topicID) and (Topic.communityID eq data.communityID)} }.firstOrNull() != null) {
                        Report.insert {
                            it[id] = UUID.randomUUID().toString()
                            it[creatorID] = data.userID
                            it[topicID] = data.topicID
                            it[communityID] = data.communityID
                            it[creationDate] =  DateTime.now()
                            it[response] = null
                            it[done] = false
                            it[reason] = data.reason
                        }
                Status(statusCode = 200, StatusMessages.OK)
            } else Status(statusCode = 404, StatusMessages.NOT_FOUND)
        } catch (e: Exception) {
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

}