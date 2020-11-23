package br.meetingplace.server.modules.global.methods.member

import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType

fun getMemberRole(members: List<MemberData>, id: String): MemberType?{
    for(i in members.indices){
        if(members[i].ID == id)
            return members[i].role
    }
    return null
}