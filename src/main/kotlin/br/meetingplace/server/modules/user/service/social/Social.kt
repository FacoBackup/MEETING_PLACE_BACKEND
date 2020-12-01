package br.meetingplace.server.modules.user.service.social

import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.modules.community.type.MemberType
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import br.meetingplace.server.modules.user.entitie.Social
import br.meetingplace.server.request.dto.generic.SubjectDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Social {

    fun follow(data: SubjectDTO): Status {
        return try {
            when(data.community){
                true-> transaction {
                    CommunityMember.insert {
                        it[communityID] = data.subjectID
                        it[userID] = data.userID
                        it[role] = MemberType.FOLLOWER.toString()
                    }
                }
                false-> transaction {
                    Social.insert {
                        it[followedID] = data.subjectID
                        it[followerID] = data.userID
                    }
                }
            }
            Status(statusCode = 200, StatusMessages.OK)
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun unfollow(data: SubjectDTO): Status {
        return try {
            when(data.community){
                true-> transaction {
                    CommunityMember.deleteWhere {(CommunityMember.userID eq data.userID) and (CommunityMember.communityID eq data.subjectID)}
                }

                false-> transaction {
                    Social.deleteWhere {(Social.followerID eq data.userID) and (Social.followedID eq data.subjectID)}
                }
            }
            Status(statusCode = 200, StatusMessages.OK)
        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
}