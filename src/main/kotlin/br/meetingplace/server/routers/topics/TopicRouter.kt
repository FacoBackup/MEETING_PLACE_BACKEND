package br.meetingplace.server.routers.topics


import br.meetingplace.server.db.mapper.community.CommunityMapper
import br.meetingplace.server.db.mapper.topic.TopicMapper
import br.meetingplace.server.db.mapper.user.UserMapper
import br.meetingplace.server.modules.topic.dao.delete.TopicDeleteDAO
import br.meetingplace.server.modules.topic.dao.dislike.TopicDislikeDAO
import br.meetingplace.server.modules.topic.dao.factory.TopicFactoryDAO
import br.meetingplace.server.modules.topic.dao.like.TopicLikeDAO
import br.meetingplace.server.requests.topics.data.TopicCreationData
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator
import br.meetingplace.server.routers.topics.paths.TopicPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.topicRouter() {
    route("/api") {
        get(TopicPaths.MY_TOPICS) {
            TODO("NOT YET IMPLEMENTED")
//            val data = call.receive<Login>()
//            val topics = UserReader.getMyTopics(data, rwUser = UserDB, rwTopic = TopicDB)
//            if (topics.isEmpty())
//                call.respond("Nothing Found.")
//            else
//                call.respond(topics)
        }
        get(TopicPaths.TOPIC) {
            TODO("NOT YET IMPLEMENTED")
//            val data = call.receive<TopicIdentifier>()
//            val search = if (!data.subTopicID.isNullOrBlank()) TopicDB.select(data.subTopicID, data.mainTopicID)
//            else TopicDB.select(data.mainTopicID, null)
//
//            if (search == null)
//                call.respond("Nothing found.")
//            else
//                call.respond(search)
        }
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
            val new = call.receive<TopicCreationData>()
            call.respond(TopicFactoryDAO.create(new, userMapper = UserMapper, communityMapper = CommunityMapper))
        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<TopicSimpleOperator>()
            call.respond(TopicDeleteDAO.deleteTopic(topic))
        }

        patch(TopicPaths.LIKE) {
            val post = call.receive<TopicSimpleOperator>()
            call.respond(TopicLikeDAO.like(post, topicMapper = TopicMapper))
        }

        patch(TopicPaths.DISLIKE) {
            val post = call.receive<TopicSimpleOperator>()
            call.respond(TopicDislikeDAO.dislike(post, topicMapper = TopicMapper))
        }

    }
}