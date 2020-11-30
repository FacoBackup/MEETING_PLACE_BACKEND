package br.meetingplace.server.routers.topics


import br.meetingplace.server.db.mapper.community.CommunityMapper
import br.meetingplace.server.db.mapper.topic.TopicMapper
import br.meetingplace.server.db.mapper.user.UserMapper
import br.meetingplace.server.modules.topic.dao.delete.TopicDeleteDAO
import br.meetingplace.server.modules.topic.dao.opinion.TopicOpinionDAO
import br.meetingplace.server.modules.topic.dao.factory.TopicFactoryDAO
import br.meetingplace.server.modules.topic.db.Topic
import br.meetingplace.server.modules.topic.db.TopicOpinions
import br.meetingplace.server.requests.generic.RequestSimple
import br.meetingplace.server.requests.topics.RequestTopicCreation
import br.meetingplace.server.requests.topics.RequestTopicSimple
import br.meetingplace.server.responses.status.Status
import br.meetingplace.server.responses.status.StatusMessages
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
            call.respond(TopicFactoryDAO.create(new, userMapper = UserMapper, communityMapper = CommunityMapper))
        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<RequestTopicSimple>()
            call.respond(TopicDeleteDAO.deleteTopic(topic))
        }
        get(TopicPaths.TOPIC) {
            val data = call.receive<RequestSimple>()
            val topics = transaction { Topic.select { Topic.creatorID eq data.userID }.map { TopicMapper.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }

        post(TopicPaths.COMMENT) {
            val new = call.receive<RequestTopicCreation>()
            call.respond(TopicFactoryDAO.createComment(new, userMapper = UserMapper, communityMapper = CommunityMapper))
        }
        get (TopicPaths.COMMENT){
            val data = call.receive<RequestTopicSimple>()
            val topics = transaction { Topic.select { Topic.mainTopicID eq data.topicID }.map { TopicMapper.mapTopic(it) } }
            if (topics.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(topics)
        }


        put(TopicPaths.LIKE) {
            val post = call.receive<RequestTopicSimple>()
            call.respond(TopicOpinionDAO.like(post, topicMapper = TopicMapper))
        }
        get(TopicPaths.LIKE) {
            val data = call.receive<RequestTopicSimple>()
            val opinions = transaction { TopicOpinions.select { (TopicOpinions.topicID eq data.topicID) and (TopicOpinions.liked eq true) }.map { TopicMapper.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404,StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
        put(TopicPaths.DISLIKE) {
            val post = call.receive<RequestTopicSimple>()
            call.respond(TopicOpinionDAO.dislike(post, topicMapper = TopicMapper))
        }
        get(TopicPaths.DISLIKE) {
            val data = call.receive<RequestTopicSimple>()
            val opinions = transaction { TopicOpinions.select { (TopicOpinions.topicID eq data.topicID) and (TopicOpinions.liked eq false) }.map { TopicMapper.mapTopicOpinions(it) } }
            if (opinions.isEmpty())
                call.respond(Status(404, StatusMessages.NOT_FOUND))
            else
                call.respond(opinions)
        }
    }
}