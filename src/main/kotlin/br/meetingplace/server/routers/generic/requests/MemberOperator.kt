package br.meetingplace.server.routers.generic.requests

data class MemberOperator(val memberEmail: String, val stepDown: Boolean?, val identifier: Identifier, val login: Login)