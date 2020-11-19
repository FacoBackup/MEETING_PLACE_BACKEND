package br.meetingplace.server.loadstore.interfaces

import br.meetingplace.server.services.groups.classes.Group

interface GroupLSInterface {
    fun read(id: String): Group?
    fun write(data: Group)
    fun delete(data: Group)
}