package br.meetingplace.server.requests.community

import br.meetingplace.server.requests.generic.Identifier
import br.meetingplace.server.requests.generic.Login

data class ApprovalData(val identifier: Identifier, val login: Login)