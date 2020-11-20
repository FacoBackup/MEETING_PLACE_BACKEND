package br.meetingplace.server.modules.user.dto.dependencies.services.topic

class UserTopics private constructor() : UserTopicsInterface {
    companion object {
        private val Class = UserTopics()
        fun getClass() = Class
    }

    private var myTopics = mutableListOf<String>()

    override fun getMyTopics() = myTopics
    override fun updateMyTopics(topic: String, add: Boolean) {
        when (add) {
            true -> {
                if (topic !in myTopics)
                    myTopics.add(topic)
            }
            false -> {
                if (topic in myTopics)
                    myTopics.remove(topic)
            }
        }

    }
}