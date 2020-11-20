package br.meetingplace.server.requests.community

import br.meetingplace.server.requests.generic.data.Identifier
import br.meetingplace.server.requests.generic.data.Login

data class Approval(val identifier: Identifier, val login: Login)