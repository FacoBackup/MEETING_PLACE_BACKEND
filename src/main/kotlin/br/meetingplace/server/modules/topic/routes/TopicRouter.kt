package br.meetingplace.server.modules.topic.routes


import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.topic.dao.topic.TopicDAO
import br.meetingplace.server.modules.topic.dao.opinion.TopicOpinionDAO
import br.meetingplace.server.modules.topic.dao.seen.TopicVisualizationDAO
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import br.meetingplace.server.modules.topic.dto.requests.RequestTopics
import br.meetingplace.server.modules.topic.services.delete.TopicDeleteService
import br.meetingplace.server.modules.topic.services.factory.TopicFactoryService
import br.meetingplace.server.modules.topic.services.opinion.TopicOpinionService
import br.meetingplace.server.modules.topic.services.read.TopicReadService
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
        patch("/get/topics/subject"){
            val data = call.receive<RequestTopics>()
            val log = call.log
            if(log != null)
                call.respond(TopicReadService.readSubjectTopics(requester = log.userID, subjectID= data.subjectID, community = data.community, topicDAO =  TopicDAO, topicVisualizationDAO = TopicVisualizationDAO, decryption = AES, timePeriod = 0))
            else call.respond(HttpStatusCode.Unauthorized)
        }
        get("/timeline/all") {
            val log = call.log
            if(log != null)
                call.respond(TopicReadService.readAllTimeline(requester = log.userID,

                    userSocialDAO = UserSocialDAO,
                    decryption = AES,
                    topicDAO = TopicDAO,
                    topicVisualizationDAO = TopicVisualizationDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }

        get("/timeline/new") {
            val log = call.log
            if(log != null)
                call.respond(TopicReadService.readNewItemsTimeline(requester = log.userID,

                    userSocialDAO = UserSocialDAO,
                    decryption = AES,
                    topicDAO = TopicDAO,
                    topicVisualizationDAO = TopicVisualizationDAO))
            else call.respond(HttpStatusCode.Unauthorized)
        }

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

        put("/topic/like") {
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicOpinionService.like(requester = log.userID, data, UserDAO, TopicDAO, TopicOpinionDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        put("/topic/dislike") {
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicOpinionService.dislike(requester = log.userID, data, UserDAO, TopicDAO, TopicOpinionDAO))
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