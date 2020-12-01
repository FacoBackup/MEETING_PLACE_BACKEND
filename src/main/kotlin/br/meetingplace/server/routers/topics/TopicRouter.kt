package br.meetingplace.server.routers.topics


import br.meetingplace.server.modules.community.dao.CommunityDAO
import br.meetingplace.server.modules.topic.dao.TopicMapper
import br.meetingplace.server.modules.user.dao.UserMapper
import br.meetingplace.server.modules.topic.service.delete.TopicDelete
import br.meetingplace.server.modules.topic.service.opinion.TopicOpinion
import br.meetingplace.server.modules.topic.service.factory.TopicFactory
import br.meetingplace.server.modules.topic.entitie.Topic
import br.meetingplace.server.modules.topic.entitie.TopicOpinions
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
            call.respond(TopicFactory.create(new, userMapper = UserMapper, communityMapper = CommunityDAO))
        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<TopicDTO>()
            call.respond(TopicDelete.deleteTopic(topic))
        }
        get(TopicPaths.TOPIC) {
            val data = call.receive<LogDTO>()
            val topics = transaction { Topic.select { Topic.creatorID eq data.userID }.map { TopicMapper.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }

        post(TopicPaths.COMMENT) {
            val new = call.receive<TopicCreationDTO>()
            call.respond(TopicFactory.createComment(new, userMapper = UserMapper, communityMapper = CommunityDAO))
        }
        get (TopicPaths.COMMENT){
            val data = call.receive<TopicDTO>()
            val topics = transaction { Topic.select { Topic.mainTopicID eq data.topicID }.map { TopicMapper.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }


        put(TopicPaths.LIKE) {
            val post = call.receive<TopicDTO>()
            call.respond(TopicOpinion.like(post, topicMapper = TopicMapper))
        }
        get(TopicPaths.LIKE) {
            val data = call.receive<TopicDTO>()
            val opinions = transaction { TopicOpinions.select { (TopicOpinions.topicID eq data.topicID) and (TopicOpinions.liked eq true) }.map { TopicMapper.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
        put(TopicPaths.DISLIKE) {
            val post = call.receive<TopicDTO>()
            call.respond(TopicOpinion.dislike(post, topicMapper = TopicMapper))
        }
        get(TopicPaths.DISLIKE) {
            val data = call.receive<TopicDTO>()
            val opinions = transaction { TopicOpinions.select { (TopicOpinions.topicID eq data.topicID) and (TopicOpinions.liked eq false) }.map { TopicMapper.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
    }
}