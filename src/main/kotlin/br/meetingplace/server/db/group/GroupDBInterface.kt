package br.meetingplace.server.db.group

import br.meetingplace.server.modules.groups.dto.Group

interface GroupDBInterface {
    fun select(id: String): Group?
    fun insert(data: Group)
    fun delete(data: Group)
    fun check(id: String): Boolean
}