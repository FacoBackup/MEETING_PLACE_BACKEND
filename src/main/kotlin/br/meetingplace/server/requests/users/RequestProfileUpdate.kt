package br.meetingplace.server.requests.users

data class RequestProfileUpdate(val imageURL: String?, val about: String?, val userID: String)