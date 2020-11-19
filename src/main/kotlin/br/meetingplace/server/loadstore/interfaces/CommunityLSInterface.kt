package br.meetingplace.server.loadstore.interfaces

import br.meetingplace.server.services.community.classes.Community

interface CommunityLSInterface {
    fun load(id: String): Community?
    fun store(data: Community)
    fun delete(data: Community)
}