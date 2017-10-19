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

fun String.asDiscoveryMessage() : DiscoveryMessage? {
    val rawSchema = JSONObject(StringFromFile(Protocol.DISCOVERY_MESSAGE_SCHEMA_ADR))
    val schema = SchemaLoader.load(rawSchema)
    return try {
        println(this)
        val json = JSONObject(this)
        schema.validate(json)
        ObjectMapper().registerModule(JsonOrgModule()).convertValue(json, DiscoveryMessage::class.java)
        //Gson().fromJson(this, DiscoveryMessage::class.java)
    } catch (e : Exception){
        println("cannot process discovery message: ${e.message}")
        null
    }
}

fun String.asDataMessage() : DataMessage? {
    val rawSchema = JSONObject(StringFromFile(Protocol.DATA_MESSAGE_SCHEMA_ADR))
    val schema = SchemaLoader.load(rawSchema)
    return try {
        schema.validate(JSONObject(this))
        Gson().fromJson(this, DataMessage::class.java)
    } catch (e : Exception){
        println("cannot process data message: ${e.message}")
        null
    }
}