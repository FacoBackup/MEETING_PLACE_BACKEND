package br.meetingplace.server.routers.community.requests

import br.meetingplace.server.routers.generic.requests.Identifier
import br.meetingplace.server.routers.generic.requests.Login

data class ApprovalData(val identifier: Identifier, val login: Login)