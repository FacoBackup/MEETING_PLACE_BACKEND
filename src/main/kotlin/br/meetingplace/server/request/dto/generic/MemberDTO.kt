package br.meetingplace.server.request.dto.generic

data class MemberDTO(val memberID: String, val subjectID: String,
                     val community: Boolean, val userID: String)