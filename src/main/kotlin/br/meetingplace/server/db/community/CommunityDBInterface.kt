package br.meetingplace.server.db.community

import br.meetingplace.server.modules.community.dto.Community

interface CommunityDBInterface {
    fun select(id: String): Community?
    fun insert(data: Community)
    fun delete(data: Community)
}