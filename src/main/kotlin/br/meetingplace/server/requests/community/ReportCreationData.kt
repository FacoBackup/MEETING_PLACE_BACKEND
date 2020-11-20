package br.meetingplace.server.requests.community

import br.meetingplace.server.requests.generic.data.Identifier
import br.meetingplace.server.requests.generic.data.Login

data class ReportCreationData(val reason: String?, val identifier: Identifier, val login: Login)
//ID == id topic
//communityID is a requirement
