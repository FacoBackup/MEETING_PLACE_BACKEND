package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.db.mapper.user.UserMapperInterface
import br.meetingplace.server.modules.community.dto.CommunityMembersDTO
import br.meetingplace.server.modules.global.http.status.Status
import br.meetingplace.server.modules.global.http.status.StatusMessages
import br.meetingplace.server.modules.user.db.Social
import br.meetingplace.server.modules.user.dto.SocialDTO
import br.meetingplace.server.requests.generic.operators.SimpleOperator
import org.jetbrains.exposed.sql.insert

object SocialDAO {

    fun follow(data: SimpleOperator): Status {
        try {
            when(data.community){
                true->{
                    TODO("Not yet implemented")
                }
                false->{
                    if(verifyuser(data.userID) && verifyuser(data.subjectID)){
                        Social.insert {
                            it[Social.followedID] = data.subjectID
                            it[Social.followerID] = data.userID
                        }
                    }
                }
            }

        }catch (e: Exception){
            Status(statusCode = 500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    fun unfollow(data: SimpleOperator, userDB: UserDBInterface):Status{

    }
}