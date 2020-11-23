package br.meetingplace.server.modules.members.dao

import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType

abstract class Members {
    private val members = mutableListOf<MemberData>()

    fun getMembers(): List<String> {
        val members = mutableListOf<String>()
        for (i in 0 until members.size) {
            if (this.members[i].role == MemberType.MODERATOR)
                members.add(this.members[i].ID)
        }
        return members
    }

    fun getModerators(): List<String> {
        val mods = mutableListOf<String>()
        for (i in 0 until members.size) {
            if (members[i].role == MemberType.MODERATOR)
                mods.add(members[i].ID)
        }
        return mods
    }

    fun updateMember(id: String, role: MemberType, remove: Boolean) {
        when (remove) {
            true -> {
                if (verifyMember(id))
                    members.remove(MemberData(id, role))
            }
            false -> {
                if (!verifyMember(id))
                    members.add(MemberData(id, role))
            }
        }
    }

    fun updateMemberRole(email: String, newRole: MemberType) {
        val index = getMemberIndex(email)
        if (index != -1)
            members[index].role = newRole
    }

    fun getMemberRole(email: String): MemberType? {
        val index = getMemberIndex(email)
        return if (index != -1) members[index].role else null
    }

    fun verifyMember(email: String): Boolean {
        for (i in 0 until members.size) {
            if (members[i].ID == email)
                return true
        }
        return false
    }

    private fun getMemberIndex(email: String): Int {
        for (i in 0 until members.size) {
            if (members[i].ID == email)
                return i
        }
        return -1
    }
}