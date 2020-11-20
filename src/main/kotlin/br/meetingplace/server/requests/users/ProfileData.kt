package br.meetingplace.server.requests.users

import br.meetingplace.server.requests.generic.Login

data class ProfileData(val gender: String?, val nationality: String?, val about: String?, val login: Login)