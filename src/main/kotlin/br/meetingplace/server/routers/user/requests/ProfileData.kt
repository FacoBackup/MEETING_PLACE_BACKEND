package br.meetingplace.server.routers.user.requests

import br.meetingplace.server.routers.generic.requests.Login

data class ProfileData(val gender: String?, val nationality: String?, val about: String?, val login: Login)