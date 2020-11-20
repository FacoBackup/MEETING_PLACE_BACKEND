package br.meetingplace.server.routers.topics

import br.meetingplace.server.db.file.community.CommunityRW
import br.meetingplace.server.db.file.topic.TopicRW
import br.meetingplace.server.db.file.user.UserRW
import br.meetingplace.server.modules.topic.dao.delete.DeleteTopic
import br.meetingplace.server.modules.topic.dao.dislike.DislikeTopic
import br.meetingplace.server.modules.topic.dao.factory.TopicFactory
import br.meetingplace.server.modules.topic.dao.like.LikeTopic
import br.meetingplace.server.modules.user.dao.search.UserReader
import br.meetingplace.server.routers.generic.requests.Login
import br.meetingplace.server.routers.topics.paths.TopicPaths
import br.meetingplace.server.routers.topics.requests.TopicData
import br.meetingplace.server.routers.topics.requests.TopicIdentifier
import br.meetingplace.server.routers.topics.requests.TopicOperationsData
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.TopicRouter (){
    route("/api"){
        get (TopicPaths.MY_TOPICS){
            val data = call.receive<Login>()
            val topics = UserReader.getClass().getMyTopics(data, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass())
            if (topics.isEmpty())
                call.respond("Nothing Found.")
            else
                call.respond(topics)
        }
        get(TopicPaths.TOPIC) {
            val data = call.receive<TopicIdentifier>()
            val search = if(!data.subTopicID.isNullOrBlank()) TopicRW.getClass().select(data.subTopicID,data.mainTopicID)
            else TopicRW.getClass().select(data.mainTopicID, null)

            if (search == null)
                call.respond("Nothing found.")
            else
                call.respond(search)
        }
        get(TopicPaths.TIMELINE) {
            val data = call.receive<Login>()
            val topics = UserReader.getClass().getMyTimeline(data,rwTopic = TopicRW.getClass(),rwUser = UserRW.getClass())
            if (topics.isEmpty())
                call.respond("Nothing Found.")
            else
                call.respond(topics)
        }
        post(TopicPaths.TOPIC) {
            val new = call.receive<TopicData>()
            call.respond(TopicFactory.getClass().create(new, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
        }
        delete(TopicPaths.TOPIC) {
            val topic = call.receive<TopicOperationsData>()
            call.respond(DeleteTopic.getClass().delete(topic, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
        }

        patch(TopicPaths.LIKE) {
            val post = call.receive<TopicOperationsData>()
            call.respond(LikeTopic.getClass().like(post,rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
        }

        patch(TopicPaths.DISLIKE) {
            val post = call.receive<TopicOperationsData>()
            call.respond(DislikeTopic.getClass().dislike(post, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
        }

    }
}