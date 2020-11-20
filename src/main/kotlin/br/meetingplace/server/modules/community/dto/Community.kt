package br.meetingplace.server.modules.community.dto

import br.meetingplace.server.modules.community.dto.dependencies.Controller
import br.meetingplace.server.modules.members.dto.MemberData
import br.meetingplace.server.modules.members.dto.MemberType


class Community(private val name: String, private val id: String, private var about: String?, creator: String, private var imageURL: String?) : Controller() {
    fun getName() = name
    fun getID() = id

    init {
        startMembers(MemberData(creator, MemberType.CREATOR))
    }

    fun updateImage(imageURL: String?){
        this.imageURL = imageURL
    }

    fun updateAbout(about: String?){
        this.about = about
    }

    fun updateModerator(data: String, requester: String, remove: Boolean?) {
        if (getMemberRole(requester) == MemberType.MODERATOR || getMemberRole(requester) == MemberType.CREATOR) {
            when (remove) {
                true -> { //REMOVE
                    if (verifyMember(data) && getMemberRole(requester) == MemberType.CREATOR)
                        updateMember(MemberData(data, MemberType.MODERATOR), true)
                }
                false -> { //ADD
                    if (data !in getModerators())
                        updateMember(MemberData(data, MemberType.MODERATOR), false)
                }
                null -> { //STEP-DOWN
                    if (data in getModerators()) {
                        updateMember(MemberData(data, MemberType.NORMAL), false)
                        updateFollower(data, false)
                    }
                }
            }
        }
    }

    fun updateFollower(data: String, remove: Boolean) {
        when (remove) {
            false -> {
                if (!verifyMember(data)) {
                    updateMember(MemberData(data, MemberType.NORMAL), false)
                }
            }
            true -> {
                if (verifyMember(data)) {
                    updateMember(MemberData(data, MemberType.NORMAL), true)
                }
            }
        }
    }

}