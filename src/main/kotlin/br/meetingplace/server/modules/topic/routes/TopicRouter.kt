package br.meetingplace.server.modules.topic.routes


import br.meetingplace.server.methods.AES
import br.meetingplace.server.modules.community.dao.member.CommunityMemberDAO
import br.meetingplace.server.modules.topic.dao.TopicDAO
import br.meetingplace.server.modules.topic.dao.opinion.TopicOpinionDAO
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import br.meetingplace.server.modules.topic.services.delete.TopicDeleteService
import br.meetingplace.server.modules.topic.services.factory.TopicFactoryService
import br.meetingplace.server.modules.topic.services.opinion.TopicOpinionService
import br.meetingplace.server.modules.user.dao.social.UserSocialDAO
import br.meetingplace.server.modules.user.dao.user.UserDAO
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.modules.user.services.social.UserSocialService
import br.meetingplace.server.server.AuthLog.log
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.topicRouter() {
    route("/api") {

        get(TopicPaths.TIMELINE) {
            TODO("NOT YET IMPLEMENTED")
//            val data = call.receive<Login>()
//            val topics = UserReader.getMyTimeline(data, rwTopic = TopicDB, rwUser = UserDB)
//            if (topics.isEmpty())
//                call.respond("Nothing Found.")
//            else
//                call.respond(topics)
        }

        post<RequestTopicCreation>(TopicPaths.TOPIC) {
            val log = call.log
            if(log != null)
                call.respond(TopicFactoryService.create(requester = log.userID, it, TopicDAO, UserDAO, CommunityMemberDAO,AES))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicDeleteService.deleteTopic(requester = log.userID, topic, TopicDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        get(TopicPaths.USER_TOPICS) {
            val log = call.log
            if(log != null){
                val topics = TopicDAO.readByUser(log.userID)
                if (topics.isEmpty())
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(topics)
            }
            else call.respond(HttpStatusCode.Unauthorized)


        }

        post<RequestTopicCreation>(TopicPaths.COMMENT) {
            val log = call.log
            if(log != null)
                call.respond(TopicFactoryService.createComment(requester = log.userID, it,TopicDAO, UserDAO, CommunityMemberDAO, AES))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        get (TopicPaths.COMMENT){
            val data = call.receive<RequestTopic>()
            val topics = TopicDAO.readAllComments(data.topicID)
            if (topics.isEmpty())
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(topics)
        }

        put(TopicPaths.LIKE) {
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicOpinionService.like(requester = log.userID, data, UserDAO, TopicDAO, TopicOpinionDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        put(TopicPaths.DISLIKE) {
            val data = call.receive<RequestTopic>()
            val log = call.log
            if(log != null)
                call.respond(TopicOpinionService.dislike(requester = log.userID, data, UserDAO, TopicDAO, TopicOpinionDAO))
            else call.respond(HttpStatusCode.Unauthorized)

        }
        get(TopicPaths.OPINIONS) {
            val data = call.receive<RequestTopic>()
            val opinions = TopicOpinionDAO.readAll(data.topicID)
            if (opinions.isEmpty())
                call.respond(HttpStatusCode.NotFound)
            else
                call.respond(opinions)
        }
    }
}