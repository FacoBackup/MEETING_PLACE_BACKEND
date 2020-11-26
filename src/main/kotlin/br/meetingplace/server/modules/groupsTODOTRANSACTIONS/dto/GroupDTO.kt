package br.meetingplace.server.modules.groupsTODOTRANSACTIONS.dto

data class GroupDTO( val communityID: String?, val id: String,
                  var name: String,var about: String?,
                  var imageURL: String?, var approved: Boolean, val creationDate: String)