package br.meetingplace.server.modules.user.dto.requests

data class RequestProfileUpdate(val imageURL: String?, val about: String?,
                                val userID: String, val name: String?,
                                val nationality: String?, val phoneNumber: String?,
                                val city: String?)