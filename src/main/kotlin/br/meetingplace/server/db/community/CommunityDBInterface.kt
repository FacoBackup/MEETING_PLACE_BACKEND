package br.meetingplace.server.db.community

import br.meetingplace.server.modules.community.dto.Community
import br.meetingplace.server.modules.global.dto.http.status.Status

interface CommunityDBInterface {
    fun select(id: String): Community?
    fun insert(data: Community): Status
    fun delete(data: Community): Status
    fun check(id: String): Boolean
}