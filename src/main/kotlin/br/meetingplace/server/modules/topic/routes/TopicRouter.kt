package br.meetingplace.server.modules.topic.routes


import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.topic.dao.archive.TopicArchiveDAO
import br.meetingplace.server.modules.topic.dao.topic.TopicDAO
import br.meetingplace.server.modules.topic.dao.opinion.TopicOpinionDAO
import br.meetingplace.server.modules.topic.dao.seen.TopicStatusDAO
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import br.meetingplace.server.modules.topic.dto.requests.RequestTopics
import br.meetingplace.server.modules.topic.dto.requests.TopicUpdateDTO
import br.meetingplace.server.modules.topic.services.archive.TopicArchiveService
import br.meetingplace.server.modules.topic.services.delete.TopicDeleteService
import br.meetingplace.server.modules.topic.services.factory.TopicFactoryService
import br.meetingplace.server.modules.topic.services.opinion.TopicOpinionService
import br.meetingplace.server.modules.topic.services.read.TopicReadService
import br.meetingplace.server.modules.topic.services.update.TopicUpdateService
import br.meetingplace.server.modules.user.dao.social.UserSocialDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.topicRouter() {
    route("/api") {
        put ("/archive/topic") {
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicArchiveService.archiveTopic(topicID = data.topicID, requester = log.userID, topicArchiveDAO = TopicArchiveDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch ("/read/archive") {
            val data = call.receive<RequestTopics>()
            call.respond(HttpStatusCode.NotImplemented)
        }
        put("/topic"){
            val data = call.receive<TopicUpdateDTO>()
            val log = call.log
            if(log != null)
                call.respond(TopicUpdateService.update(
                    requester = log.userID,
                    header = data.header,
                    body = data.body,
                    topicID = data.topicID,
                    topicDAO = TopicDAO,
                    encryption = AES
                    ))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        //FETCHING
        patch("/fetch/topics/subject"){
            val data = call.receive<RequestTopics>()
            val log = call.log
            if(log != null)
                call.respond(TopicReadService.readSubjectTopicsByTimePeriod(
                    timePeriod = data.timePeriod,
                    subjectID= data.subjectID,
                    community = data.community,
                    topicDAO =  TopicDAO,
                    decryption = AES,
                    userDAO = UserDAO,
                    communityDAO = CommunityDAO,
                    topicOpinionDAO = TopicOpinionDAO,
                    requester = log.userID,
                    topicArchiveDAO = TopicArchiveDAO
                ))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/fetch/timeline") {
            val data = call.receive<RequestTopics>()
            val log = call.log
            if(log != null)
                call.respond(TopicReadService.readTimelineByTimePeriod(requester = log.userID,
                    timePeriod = data.timePeriod,
                    userSocialDAO = UserSocialDAO,
                    decryption = AES,
                    topicDAO = TopicDAO,
                    topicStatusDAO = TopicStatusDAO,
                    userDAO = UserDAO,
                    communityDAO = CommunityDAO,
                    communityMemberDAO = CommunityMemberDAO,
                    topicOpinionDAO = TopicOpinionDAO,
                    topicArchiveDAO = TopicArchiveDAO
                    ))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        //FETCHING
        post<RequestTopicCreation>("/topic") {
            val log = call.log

            if(log != null)
                call.respond(TopicFactoryService.create(requester = log.userID, it, TopicDAO, UserDAO, CommunityMemberDAO,AES))
            else call.respond(HttpStatusCode.Unauthorized)

        }


        delete("/topic") {
            val topic = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicDeleteService.deleteTopic(requester = log.userID, topic, TopicDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        post<RequestTopicCreation>("/topic/comment") {
            val log = call.log
            if(log != null)
                call.respond(TopicFactoryService.createComment(requester = log.userID, it,
                    TopicDAO, UserDAO, CommunityMemberDAO, AES))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        get ("/topic/comment"){
            val data = call.receive<RequestTopic>()
            val topics = TopicDAO.readAllComments(data.topicID)
            if (topics.isEmpty())
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(topics)
        }

        put("/like") {
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicOpinionService.like(requester = log.userID, data, UserDAO, TopicDAO, TopicOpinionDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        put("/dislike") {
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicOpinionService.dislike(requester = log.userID, data, UserDAO, TopicDAO, TopicOpinionDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/check/like"){
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null){
                val response = TopicOpinionDAO.read(topicID = data.topicID, userID = log.userID)
                if(response == null)
                    call.respond(false)
                else
                    call.respond(response.liked)
            }

            else call.respond(HttpStatusCode.Unauthorized)
        }
        patch("/check/dislike"){
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null){
                val response = TopicOpinionDAO.read(topicID = data.topicID, userID = log.userID)
                if(response == null)
                    call.respond(false)
                else
                    call.respond(!response.liked)
            }
            else call.respond(HttpStatusCode.Unauthorized)
        }
        get("/topic/opinions") {
            val data = call.receive<RequestTopic>()
            val opinions = TopicOpinionDAO.readAll(data.topicID)
            if (opinions.isEmpty())
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(opinions)
        }
    }
}