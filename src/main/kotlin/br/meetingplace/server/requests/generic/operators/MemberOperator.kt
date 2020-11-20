package br.meetingplace.server.requests.generic.operators

import br.meetingplace.server.requests.generic.data.Identifier
import br.meetingplace.server.requests.generic.data.Login

data class MemberOperator(val memberEmail: String, val stepDown: Boolean?, val identifier: Identifier, val login: Login)