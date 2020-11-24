package br.meetingplace.server.modules.community.dto

import java.time.LocalDateTime

data class CommunityDTO (var name: String, val id: String, var about: String?,
                         var imageURL: String?,val creationDate: LocalDateTime)