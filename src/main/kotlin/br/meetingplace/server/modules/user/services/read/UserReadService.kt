package br.meetingplace.server.modules.user.services.read

import br.meetingplace.server.modules.topic.dao.topic.TI
import br.meetingplace.server.modules.user.dao.social.SI
import br.meetingplace.server.modules.user.dao.user.UI
import br.meetingplace.server.modules.user.dto.response.UserProfileDTO

object UserReadService {
    suspend fun read(userID: String, userSocialDAO: SI, userDAO: UI, topicDAO: TI): UserProfileDTO?{
        return try{
            val user = userDAO.readByID(userID)
            if(user != null){
                val followers = userSocialDAO.readAll(userID, false).size
                val following = userSocialDAO.readAll(userID, true).size
                val topics = topicDAO.readTopicsQuantityByUser(userID)

                UserProfileDTO(
                    name = user.name,
                    email= user.email,
                    about = user.about,
                    nationality = user.nationality,
                    cityOfBirth = user.cityOfBirth,
                    joinedIn = user.joinedIn,
                    following = following.toLong(),
                    followers = followers.toLong(),
                    topics = topics,
                    backgroundImageURL = user.backgroundImageURL,
                    imageURL = user.imageURL,
                    birthDate = user.birthDate,
                    phoneNumber = user.phoneNumber,
                    gender = user.gender,
                    admin = user.admin
                )

            }
            else
                null
        }catch (e: Exception){
            null
        }
    }
}