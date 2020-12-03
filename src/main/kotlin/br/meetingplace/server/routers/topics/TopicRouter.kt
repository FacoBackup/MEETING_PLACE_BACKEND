package br.meetingplace.server.routers.topics


import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.topic.dao.TopicDAO
import br.meetingplace.server.modules.user.dao.UserDAO
import br.meetingplace.server.modules.topic.services.delete.TopicDeleteService
import br.meetingplace.server.modules.topic.services.opinion.TopicOpinionService
import br.meetingplace.server.modules.topic.services.factory.TopicFactoryService
import br.meetingplace.server.modules.topic.entities.Topic
import br.meetingplace.server.modules.topic.entities.TopicOpinion
import br.meetingplace.server.modules.user.dto.requests.RequestUser
import br.meetingplace.server.modules.topic.dto.requests.RequestTopicCreation
import br.meetingplace.server.modules.topic.dto.requests.RequestTopic
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

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

        post(TopicPaths.TOPIC) {
            val new = call.receive<RequestTopicCreation>()
            call.respond(TopicFactoryService.create(new, userMapper = UserDAO, communityMapper = CommunityDAO))
        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<RequestTopic>()
            call.respond(TopicDeleteService.deleteTopic(topic))
        }
        get(TopicPaths.TOPIC) {
            val data = call.receive<RequestUser>()
            val topics = transaction { Topic.select { Topic.creatorID eq data.userID }.map { TopicDAO.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }

        post(TopicPaths.COMMENT) {
            val new = call.receive<RequestTopicCreation>()
            call.respond(TopicFactoryService.createComment(new, userMapper = UserDAO, communityMapper = CommunityDAO))
        }
        get (TopicPaths.COMMENT){
            val data = call.receive<RequestTopic>()
            val topics = transaction { Topic.select { Topic.mainTopicID eq data.topicID }.map { TopicDAO.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }


        put(TopicPaths.LIKE) {
            val post = call.receive<RequestTopic>()
            call.respond(TopicOpinionService.like(post, topicMapper = TopicDAO))
        }
        get(TopicPaths.LIKE) {
            val data = call.receive<RequestTopic>()
            val opinions = transaction { TopicOpinion.select { (TopicOpinion.topicID eq data.topicID) and (TopicOpinion.liked eq true) }.map { TopicDAO.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
        put(TopicPaths.DISLIKE) {
            val post = call.receive<RequestTopic>()
            call.respond(TopicOpinionService.dislike(post, topicMapper = TopicDAO))
        }
        get(TopicPaths.DISLIKE) {
            val data = call.receive<RequestTopic>()
            val opinions = transaction { TopicOpinion.select { (TopicOpinion.topicID eq data.topicID) and (TopicOpinion.liked eq false) }.map { TopicDAO.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
    }
}