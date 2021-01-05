package br.meetingplace.server.modules.reportTODO.services.factory

import br.meetingplace.server.modules.community.dao.member.CMI
import br.meetingplace.server.modules.reportTODO.dao.RI
import br.meetingplace.server.modules.reportTODO.dto.requests.RequestReportCreation
import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.user.dao.user.UI
import io.ktor.http.*

object ReportFactoryService{

    suspend fun createReport(requester: String, data: RequestReportCreation, reportDAO: RI, userDAO: UI, topicDAO: TI, communityMemberDAO: CMI): HttpStatusCode {
        return try {
            val topic = topicDAO.read(data.topicID)
            return if(userDAO.check(requester) &&
                      communityMemberDAO.check(data.communityID, userID = requester) == HttpStatusCode.Found &&
                      topic != null && topic.communityID == data.communityID)
                          reportDAO.create(requester = requester,data)
            else HttpStatusCode.FailedDependency
        } catch (e: Exception) {
            HttpStatusCode.InternalServerError
        }
    }

}