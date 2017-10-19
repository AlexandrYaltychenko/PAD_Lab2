package protocol

import org.everit.json.schema.loader.SchemaLoader
import org.json.JSONObject
import util.StringFromFile

internal class EncoderKtTest {
    @org.junit.jupiter.api.Test
    fun asDiscoveryMessage() {
        val str = StringFromFile("schema/discovery_message.json")
        val schema = SchemaLoader.load(JSONObject(str))
        val json = StringFromFile("test.json")
        println(json)
        schema.validate(JSONObject(json))
    }

    @org.junit.jupiter.api.Test
    fun testDataParse(){
        val str = StringFromFile("test.json")
        val schema = SchemaLoader.load(JSONObject(StringFromFile(Protocol.DATA_MESSAGE_SCHEMA_ADR)))
        val json = JSONObject(str)
        schema.validate(json)
    }

}