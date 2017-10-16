package protocol

import com.google.gson.Gson
import org.everit.json.schema.loader.SchemaLoader
import org.json.JSONObject
import protocol.message.DiscoveryMessage
import util.StringFromFile

fun DiscoveryMessage.encode() =
    Gson().toJson(this)

fun String.asDiscoveryMessage() : DiscoveryMessage? {
    val rawSchema = JSONObject(StringFromFile("schema/discovery_message.json"))
    val schema = SchemaLoader.load(rawSchema)
    return try {
        schema.validate(JSONObject(this))
        Gson().fromJson(this, DiscoveryMessage::class.java)
    } catch (e : Exception){
        null
    }
}