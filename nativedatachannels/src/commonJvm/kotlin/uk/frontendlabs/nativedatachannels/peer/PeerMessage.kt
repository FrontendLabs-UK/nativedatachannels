package uk.frontendlabs.nativedatachannels.peer

import org.json.JSONObject

sealed class PeerMessage {
    abstract fun toJson(): String

    companion object {
        fun fromJson(json: String): PeerMessage {
            val obj = JSONObject(json)
            val type = obj.getString("_type")
            return when (type) {
                "CreateGroup" -> CreateGroup
                "JoinGroup" -> JoinGroup(obj.getString("group"), obj.getString("sdp"), obj.getString("type"))
                "GroupInfo" -> GroupInfo(obj.getString("phrase"))
                "AnswerGroup" -> AnswerGroup(obj.getString("group"), obj.getString("sdp"), obj.getString("type"))
                "LocalCandidate" -> LocalCandidate(obj.getString("group"), obj.getString("candidate"), obj.getString("mid"), obj.getString("isFor"))
                else -> throw RuntimeException("Unknown message type: $json")
            }
        }
    }
}

data object CreateGroup: PeerMessage() {
    override fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("_type", "CreateGroup")
        return jsonObject.toString()
    }
}

data class GroupInfo(val phrase: String): PeerMessage() {
    override fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("_type", "GroupInfo")
        jsonObject.put("phrase", phrase)
        return jsonObject.toString()
    }
}

data class JoinGroup(val group: String, val sdp: String, val type: String): PeerMessage() {
    override fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("_type", "JoinGroup")
        jsonObject.put("group", group)
        jsonObject.put("sdp", sdp)
        jsonObject.put("type", type)
        return jsonObject.toString()
    }
}

data class AnswerGroup(val group: String, val sdp: String, val type: String): PeerMessage() {
    override fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("_type", "AnswerGroup")
        jsonObject.put("group", group)
        jsonObject.put("sdp", sdp)
        jsonObject.put("type", type)
        return jsonObject.toString()
    }
}

data class LocalCandidate(
    val group: String,
    val candidate: String,
    val mid: String,
    val isFor: String
): PeerMessage() {
    override fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("_type", "LocalCandidate")
        jsonObject.put("group", group)
        jsonObject.put("candidate", candidate)
        jsonObject.put("mid", mid)
        jsonObject.put("isFor", isFor)
        return jsonObject.toString()
    }
}