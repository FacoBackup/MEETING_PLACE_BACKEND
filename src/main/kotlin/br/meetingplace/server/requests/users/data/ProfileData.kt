package br.meetingplace.server.requests.users.data

import br.meetingplace.server.requests.generic.data.Login

data class ProfileData(val gender: String?, val imageURL: String?, val nationality: String?, val about: String?, val login: Login)