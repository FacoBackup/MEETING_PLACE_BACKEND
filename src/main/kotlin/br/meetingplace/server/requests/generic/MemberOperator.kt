package br.meetingplace.server.requests.generic

data class MemberOperator(val memberEmail: String, val stepDown: Boolean?, val identifier: Identifier, val login: Login)