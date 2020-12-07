package br.meetingplace.server.modules.reportTODO.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.reportTODO.dao.RI
import br.meetingplace.server.modules.reportTODO.dto.requests.RequestReportCreation
import br.meetingplace.server.modules.topic.dao.TI
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object ReportFactoryService{

    fun createReport(data: RequestReportCreation, reportDAO: RI, userDAO: UI, topicDAO: TI, communityMemberDAO: CMI): HttpStatusCode {
        return try {
            val topic = topicDAO.read(data.topicID)
            return if(userDAO.check(data.userID) &&
                      communityMemberDAO.check(data.communityID, userID = data.userID) == HttpStatusCode.Found &&
                      topic != null && topic.communityID == data.communityID)
                          reportDAO.create(data)
            else HttpStatusCode.FailedDependency
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }

}