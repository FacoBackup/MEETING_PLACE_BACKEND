package br.meetingplace.server.requests.generic

data class MemberOperator(val memberID: String, val subjectID: String,
                          val community: Boolean, val userID: String)