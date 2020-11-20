package br.meetingplace.server.routers.chat.requests

import br.meetingplace.server.routers.generic.requests.Login

class ChatFinderOperator(val login: Login, val identifier: ChatIdentifier)