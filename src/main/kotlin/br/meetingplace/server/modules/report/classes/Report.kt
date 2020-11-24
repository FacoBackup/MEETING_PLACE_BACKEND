package br.meetingplace.server.modules.report.classes

import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime

class Report(
        private val reportID: String,
        private val creatorID: String,
        private val serviceID: String,
        private val reason: String?,
        private val communityID: String,
        private val creationDate: LocalDateTime,
        private var status: ReportStatus,
        private var response: String?
): Table(){
        fun getCreationDate() = creationDate
        fun getID() = reportID
        fun getCreatorID () = creatorID
        fun getServiceID () = serviceID
        fun getReason () = reason
        fun getStatus () = status
        fun getCommunityID () = communityID
        fun getResponse () = response

        fun setResponse(response: String){
                this.response = response
        }
        fun setStatus(status: ReportStatus){
                this.status = status
        }
}
