package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.community.db.CommunityMember
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
import br.meetingplace.server.modules.user.db.Social
import br.meetingplace.server.requests.generic.operators.SimpleOperator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object SocialDAO {

    fun follow(data: SimpleOperator): Status {
        return try {
            when(data.community){
                true-> transaction {
                    CommunityMember.insert {
                        it[communityID] = data.subjectID
                        it[userID] = data.userID
                        it[admin] = false
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

    fun unfollow(data: SimpleOperator): Status {
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