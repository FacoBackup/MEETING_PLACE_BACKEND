package br.meetingplace.server.modules.community.dto

data class CommunityDTO (var name: String, val id: String, var about: String?,
                         var imageURL: String?,val creationDate: String)