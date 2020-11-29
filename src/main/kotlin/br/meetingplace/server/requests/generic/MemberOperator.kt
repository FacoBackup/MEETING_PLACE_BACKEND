package br.meetingplace.server.requests.generic

data class MemberOperator(val memberEmail: String, val stepDown: Boolean?, val subjectID: String, val community: Boolean, val userID: String)