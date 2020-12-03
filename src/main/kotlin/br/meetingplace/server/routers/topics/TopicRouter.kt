package br.meetingplace.server.routers.topics


import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.topic.dao.TopicDAO
import br.meetingplace.server.modules.user.dao.UserDAO
import br.meetingplace.server.modules.topic.service.delete.TopicDeleteService
import br.meetingplace.server.modules.topic.service.opinion.TopicOpinionService
import br.meetingplace.server.modules.topic.service.factory.TopicFactoryService
import br.meetingplace.server.modules.topic.entitie.Topic
import br.meetingplace.server.modules.topic.entitie.TopicOpinion
import br.meetingplace.server.request.dto.generic.LogDTO
import br.meetingplace.server.request.dto.topics.TopicCreationDTO
import br.meetingplace.server.request.dto.topics.TopicDTO
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
            val new = call.receive<TopicCreationDTO>()
            call.respond(TopicFactoryService.create(new, userMapper = UserDAO, communityMapper = CommunityDAO))
        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<TopicDTO>()
            call.respond(TopicDeleteService.deleteTopic(topic))
        }
        get(TopicPaths.TOPIC) {
            val data = call.receive<LogDTO>()
            val topics = transaction { Topic.select { Topic.creatorID eq data.userID }.map { TopicDAO.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }

        post(TopicPaths.COMMENT) {
            val new = call.receive<TopicCreationDTO>()
            call.respond(TopicFactoryService.createComment(new, userMapper = UserDAO, communityMapper = CommunityDAO))
        }
        get (TopicPaths.COMMENT){
            val data = call.receive<TopicDTO>()
            val topics = transaction { Topic.select { Topic.mainTopicID eq data.topicID }.map { TopicDAO.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }


        put(TopicPaths.LIKE) {
            val post = call.receive<TopicDTO>()
            call.respond(TopicOpinionService.like(post, topicMapper = TopicDAO))
        }
        get(TopicPaths.LIKE) {
            val data = call.receive<TopicDTO>()
            val opinions = transaction { TopicOpinion.select { (TopicOpinion.topicID eq data.topicID) and (TopicOpinion.liked eq true) }.map { TopicDAO.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
        put(TopicPaths.DISLIKE) {
            val post = call.receive<TopicDTO>()
            call.respond(TopicOpinionService.dislike(post, topicMapper = TopicDAO))
        }
        get(TopicPaths.DISLIKE) {
            val data = call.receive<TopicDTO>()
            val opinions = transaction { TopicOpinion.select { (TopicOpinion.topicID eq data.topicID) and (TopicOpinion.liked eq false) }.map { TopicDAO.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
    }
}