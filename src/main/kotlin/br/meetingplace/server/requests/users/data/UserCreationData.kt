package br.meetingplace.server.requests.users.data

data class UserCreationData(val userName: String, val imageURL: String?, val age: Int, val email: String, val password: String)