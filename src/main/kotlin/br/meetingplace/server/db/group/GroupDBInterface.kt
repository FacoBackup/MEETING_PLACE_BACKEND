package br.meetingplace.server.db.group

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.groups.classes.Group

interface GroupDBInterface {
    fun select(id: String): Group?
    fun insert(data: Group): Status
    fun delete(data: Group): Status
    fun check(id: String): Boolean
}