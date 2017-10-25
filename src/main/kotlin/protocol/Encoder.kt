package protocol

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule
import com.google.gson.Gson
import org.everit.json.schema.Schema
import org.everit.json.schema.loader.SchemaLoader
import org.json.JSONObject
import protocol.message.DataMessage
import protocol.message.DiscoveryMessage
import protocol.message.Message
import util.StringFromFile

fun Message.encode() =
        Gson().toJson(this)

fun String.asDiscoveryMessage(): DiscoveryMessage? {
    val rawSchema = JSONObject(StringFromFile(Protocol.DISCOVERY_MESSAGE_SCHEMA_ADR))
    val schema = SchemaLoader.load(rawSchema)
    return try {
        val json = JSONObject(this)
        schema.validate(json)
        ObjectMapper().registerModule(JsonOrgModule()).convertValue(json, DiscoveryMessage::class.java)
        //Gson().fromJson(this, DiscoveryMessage::class.java)
    } catch (e: Exception) {
        println("cannot process discovery message: ${e.message}")
        null
    }
}

fun String.asDataMessage(): DataMessage? {
    val rawSchema = JSONObject(StringFromFile(Protocol.DATA_MESSAGE_SCHEMA_ADR))
    val schema = SchemaLoader.load(rawSchema)
    return try {
        schema.validate(JSONObject(this))
        Gson().fromJson(this, DataMessage::class.java)
    } catch (e: Exception) {
        println("cannot process data message: ${e.message}")
        null
    }
}

fun String.asQuery(): Query? {
    val query = this.removeSurrounding(" ")
    val parsedQuery = query.split("(", ")", ",").map { it.trim() }.filter { !it.contains(" ") && it.isNotEmpty() }
    val select = mutableSetOf<String>()
    val order = mutableSetOf<String>()
    val group = mutableSetOf<String>()
    val filter = mutableSetOf<String>()
    var currentSet: MutableSet<String>? = null
    parsedQuery.forEach {
        if (Protocol.DSL_KEYWORDS.contains(it)) {
            currentSet = when (it.toLowerCase()) {
                "sort" -> order
                "group" -> group
                "filter" -> filter
                else -> select
            }
        } else {
            if (currentSet != null) {
                currentSet?.add(it)
            } else {
                println("INVALID QUERY")
                return null
            }
        }
    }
    return Query(order, filter, group, select)
}