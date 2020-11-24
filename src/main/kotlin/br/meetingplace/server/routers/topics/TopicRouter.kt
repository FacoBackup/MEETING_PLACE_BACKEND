package br.meetingplace.server.routers.topics

import br.meetingplace.server.db.community.CommunityDB
import br.meetingplace.server.db.topic.TopicDB
import br.meetingplace.server.db.user.UserDB
import br.meetingplace.server.modules.topic.dao.delete.DeleteTopic
import br.meetingplace.server.modules.topic.dao.dislike.DislikeTopic
import br.meetingplace.server.modules.topic.dao.factory.TopicFactory
import br.meetingplace.server.modules.topic.dao.like.LikeTopic
import br.meetingplace.server.modules.user.dao.todo.UserReader
import br.meetingplace.server.requests.generic.data.Login
import br.meetingplace.server.requests.topics.data.TopicData
import br.meetingplace.server.requests.topics.data.TopicIdentifier
import br.meetingplace.server.requests.topics.operators.TopicSimpleOperator
import br.meetingplace.server.routers.topics.paths.TopicPaths
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.topicRouter() {
    route("/api") {
        get(TopicPaths.MY_TOPICS) {
            val data = call.receive<Login>()
            val topics = UserReader.getClass().getMyTopics(data, rwUser = UserDB.getClass(), rwTopic = TopicDB.getClass())
            if (topics.isEmpty())
                call.respond("Nothing Found.")
            else
                call.respond(topics)
        }
        get(TopicPaths.TOPIC) {
            val data = call.receive<TopicIdentifier>()
            val search = if (!data.subTopicID.isNullOrBlank()) TopicDB.getClass().select(data.subTopicID, data.mainTopicID)
            else TopicDB.getClass().select(data.mainTopicID, null)

            if (search == null)
                call.respond("Nothing found.")
            else
                call.respond(search)
        }
        get(TopicPaths.TIMELINE) {
            val data = call.receive<Login>()
            val topics = UserReader.getClass().getMyTimeline(data, rwTopic = TopicDB.getClass(), rwUser = UserDB.getClass())
            if (topics.isEmpty())
                call.respond("Nothing Found.")
            else
                call.respond(topics)
        }
        post(TopicPaths.TOPIC) {
            val new = call.receive<TopicData>()
            call.respond(TopicFactory.getClass().create(new, userDB = UserDB.getClass(), topicDB = TopicDB.getClass(), communityDB = CommunityDB.getClass()))
        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<TopicSimpleOperator>()
            call.respond(DeleteTopic.getClass().delete(topic, rwUser = UserDB.getClass(), rwTopic = TopicDB.getClass(), rwCommunity = CommunityDB.getClass()))
        }

        patch(TopicPaths.LIKE) {
            val post = call.receive<TopicSimpleOperator>()
            call.respond(LikeTopic.getClass().like(post, userDB = UserDB.getClass(), topicDB = TopicDB.getClass(), communityDB = CommunityDB.getClass()))
        }

        patch(TopicPaths.DISLIKE) {
            val post = call.receive<TopicSimpleOperator>()
            call.respond(DislikeTopic.getClass().dislike(post, rwUser = UserDB.getClass(), rwTopic = TopicDB.getClass(), rwCommunity = CommunityDB.getClass()))
        }

    }
}