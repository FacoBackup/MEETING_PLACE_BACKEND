package br.meetingplace.server.routers.community.requests

import br.meetingplace.server.routers.generic.requests.Identifier
import br.meetingplace.server.routers.generic.requests.Login

data class ReportData(val reason: String?, val identifier: Identifier, val login: Login)
//ID == id topic
//communityID is a requirement
