package br.meetingplace.server.modules.report.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.report.dao.RI
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.report.dto.requests.RequestReportCreation

object ReportFactoryService{

    fun createReport(data: RequestReportCreation, reportDAO: RI, userDAO: UI, topicDAO: TI, communityMemberDAO: CMI): Status {
        return try {
            val topic = topicDAO.read(data.topicID)
            return if(userDAO.read(data.userID) != null &&
                      communityMemberDAO.read(data.communityID, userID = data.userID) != null &&
                      topic != null && topic.communityID == data.communityID)
                          reportDAO.create(data)
            else Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        } catch (e: Exception) {
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

}