package br.meetingplace.server.requests.community

import br.meetingplace.server.requests.generic.Identifier
import br.meetingplace.server.requests.generic.Login

data class ReportData(val reason: String?, val identifier: Identifier, val login: Login)
//ID == id topic
//communityID is a requirement
